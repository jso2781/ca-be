package kr.or.kids.domain.ca.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.or.kids.domain.ca.common.file.mapper.FileMapper;
import kr.or.kids.domain.ca.common.file.service.FileCryptoService;
import kr.or.kids.domain.ca.common.file.service.FileService;
import kr.or.kids.domain.ca.common.file.vo.FileDataReqVO;
import kr.or.kids.domain.ca.common.file.vo.FileDataResVO;
import kr.or.kids.domain.ca.common.file.vo.FileDownloadLogReqVO;
import kr.or.kids.domain.ca.common.file.vo.FileMetaVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/ca/file")
//@RequestMapping("/file")
public class FileDownController {

    @Value("${file.storePath}")
    private String fileStorePath;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FileService fileService;

    @Autowired
    FileCryptoService cryptoService;

    /**
     * 파일 다운로드 (ResponseEntity 방식)
     * @param filename 다운로드할 파일명
     * @return ResponseEntity<Resource>
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam String filename) {
        try {
            // 1. 파일명 검증 (경로 조작 공격 방지)
            if (!isValidFilename(filename)) {
                log.error("Invalid filename detected: {}", filename);
                return ResponseEntity.badRequest().build();
            }

            // 2. 파일 경로 생성
            Path filePath = Paths.get(fileStorePath).resolve(filename).normalize();
            File file = filePath.toFile();

            // 3. 파일 존재 여부 확인
            if (!file.exists() || !file.isFile()) {
                log.error("File not found: {}", filePath);
                return ResponseEntity.notFound().build();
            }

            // 4. 보안: 파일이 지정된 디렉토리 내에 있는지 확인 (Path Traversal 방지)
            if (!filePath.startsWith(Paths.get(fileStorePath).normalize())) {
                log.error("Access denied - file outside allowed directory: {}", filePath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // 5. 파일을 Resource로 변환
            Resource resource = new FileSystemResource(file);

            // 6. Content-Type 자동 감지
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 7. 한글 파일명 인코딩 처리 (RFC 5987 표준)
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");

            // 8. 응답 헤더 설정 (모든 브라우저 호환)
            HttpHeaders headers = new HttpHeaders();
            
            // RFC 5987: filename*=UTF-8''encoded-filename (IE 제외 모든 모던 브라우저)
            // filename="original-filename" (IE 지원용)
            String contentDisposition = String.format(
                "attachment; filename=\"%s\"; filename*=UTF-8''%s",
                encodedFilename,  // IE용 (fallback)
                encodedFilename   // 모던 브라우저용 (UTF-8 지원)
            );
            headers.set(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));

            log.info("File download started: {}, size: {} bytes", filename, file.length());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            log.error("File download error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 파일 다운로드 (OutputStream 방식 - 대용량 파일에 적합)
     * @param filename 다운로드할 파일명
     * @param response HttpServletResponse
     */
    @GetMapping("/download-stream")
    public void downloadStream(@RequestParam String filename, HttpServletResponse response) {
        OutputStream outputStream = null;

        try {
            log.info("=== 다운로드 시작 - filename: {}", filename);

            // 1. 파일명 검증
            if (!isValidFilename(filename)) {
                log.error("Invalid filename detected: {}", filename);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid filename");
                return;
            }

            // 2. DB에서 파일 정보 조회
            FileDataReqVO fileParam = new FileDataReqVO();
            fileParam.setSrvrFileNm(filename);
            FileDataResVO fileData = fileMapper.data(fileParam);

            if (fileData == null) {
                log.error("File data not found in DB: {}", filename);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File data not found");
                return;
            }

            String fileurl = fileData.getFileStrgPathDsctn();
            String originalFilename = fileData.getFileNm();
            String prvcInclYn = fileData.getPrvcInclYn();
            boolean isEncrypted = "1".equals(prvcInclYn) || "Y".equalsIgnoreCase(prvcInclYn);

            log.info("File info - stored: {}, original: {}, encrypted: {}",
                    filename, originalFilename, isEncrypted);

            // 3. 파일 경로 생성
            Path filePath = Paths.get(fileStorePath, fileurl, filename).normalize();

            log.info("파일 경로: {}", filePath);
            log.info("파일 존재: {}", Files.exists(filePath));

            // 4. 파일 존재 여부 확인
            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                log.error("File not found: {}", filePath);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
                return;
            }

            // 5. 보안 검사
            Path baseDir = Paths.get(fileStorePath).normalize();
            if (!filePath.startsWith(baseDir)) {
                log.error("Access denied - file outside allowed directory: {}", filePath);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }

            // 6. 파일 읽기 및 복호화 처리
            byte[] fileDataBytes;

            if (isEncrypted) {
                log.info("암호화된 파일 복호화 시작");
                byte[] encryptedData = Files.readAllBytes(filePath);
                log.info("암호화 데이터 크기: {} bytes", encryptedData.length);

                try {
                    // 복호화
                    fileDataBytes = cryptoService.decrypt(encryptedData);
                    log.info("복호화 완료 - 크기: {} bytes", fileDataBytes.length);

                    // ⭐ 복호화된 데이터 확인 (처음 100바이트)
                    if (fileDataBytes.length > 0) {
                        int previewLen = Math.min(100, fileDataBytes.length);
                        String preview = new String(fileDataBytes, 0, previewLen, StandardCharsets.UTF_8);
                        log.info("복호화된 데이터 미리보기: {}", preview);
                    }
                } catch (Exception e) {
                    log.error("복호화 실패!", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "파일 복호화 실패: " + e.getMessage());
                    return;
                }
            } else {
                log.info("일반 파일 읽기");
                fileDataBytes = Files.readAllBytes(filePath);
                log.info("파일 크기: {} bytes", fileDataBytes.length);
            }

            // ⭐ 7. Content-Type 설정 - 파일 확장자 기반으로 결정
            String contentType = getContentTypeByExtension(originalFilename);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            response.setContentType(contentType);
            log.info("Content-Type: {}", contentType);

            // 8. 파일명 인코딩 처리 (한글 지원)
            String downloadFilename = originalFilename != null ? originalFilename : filename;
            String encodedFilename = URLEncoder.encode(downloadFilename, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");

            // 9. 응답 헤더 설정
            String contentDisposition = String.format(
                    "attachment; filename=\"%s\"; filename*=UTF-8''%s",
                    encodedFilename,
                    encodedFilename
            );
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileDataBytes.length));

            // ⭐ 캐시 방지 헤더
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");

            // 10. 데이터 전송 (복호화된 데이터)
            outputStream = response.getOutputStream();
            outputStream.write(fileDataBytes);
            outputStream.flush();

            log.info("다운로드 완료 - file: {}, size: {} bytes, encrypted: {}",
                    downloadFilename, fileDataBytes.length, isEncrypted);

        } catch (Exception e) {
            log.error("File download error: ", e);
            try {
                if (!response.isCommitted()) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "File download failed: " + e.getMessage());
                }
            } catch (IOException ex) {
                log.error("Error sending error response", ex);
            }
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("Error closing output stream", e);
                }
            }
        }
    }

    // ⭐ Content-Type 결정 헬퍼 메서드
    private String getContentTypeByExtension(String filename) {
        if (filename == null) {
            return null;
        }

        String extension = "";
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0) {
            extension = filename.substring(lastDot + 1).toLowerCase();
        }

        switch (extension) {
            case "sql":
                return "text/plain; charset=UTF-8";
            case "txt":
                return "text/plain; charset=UTF-8";
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "zip":
                return "application/zip";
            case "json":
                return "application/json; charset=UTF-8";
            case "xml":
                return "text/xml; charset=UTF-8";
            case "csv":
                return "text/csv; charset=UTF-8";
            default:
                return "application/octet-stream";
        }
    }


//    @GetMapping("/download-stream")
//    public void downloadStream(@RequestParam String filename, HttpServletResponse response) {
//        OutputStream outputStream = null;
//
//        try {
//            log.info("=== 다운로드 시작 - filename: {}", filename);
//
//            // 1. 파일명 검증
//            if (!isValidFilename(filename)) {
//                log.error("Invalid filename detected: {}", filename);
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid filename");
//                return;
//            }
//
//            // 2. DB에서 파일 정보 조회
//            FileDataReqVO fileParam = new FileDataReqVO();
//            fileParam.setSrvrFileNm(filename);
//            FileDataResVO fileData = fileMapper.data(fileParam);
//
//            if (fileData == null) {
//                log.error("File data not found in DB: {}", filename);
//                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File data not found");
//                return;
//            }
//
//            String fileurl = fileData.getFileStrgPathDsctn();
//            String originalFilename = fileData.getFileNm();
//
//            // ⭐ 암호화 여부 확인
//            String prvcInclYn = fileData.getPrvcInclYn();
//            boolean isEncrypted = "1".equals(prvcInclYn) || "Y".equalsIgnoreCase(prvcInclYn);
//
//            log.info("File info - stored: {}, original: {}, encrypted: {}",
//                    filename, originalFilename, isEncrypted);
//
//            // 3. 파일 경로 생성
//            // ⭐ 경로 조합 방식 수정 (Paths.get 사용)
//            Path filePath = Paths.get(fileStorePath, fileurl, filename).normalize();
//
//            log.info("파일 경로: {}", filePath);
//            log.info("파일 존재: {}", Files.exists(filePath));
//
//            // 4. 파일 존재 여부 확인
//            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
//                log.error("File not found: {}", filePath);
//                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
//                return;
//            }
//
//            // 5. 보안 검사
//            Path baseDir = Paths.get(fileStorePath).normalize();
//            if (!filePath.startsWith(baseDir)) {
//                log.error("Access denied - file outside allowed directory: {}", filePath);
//                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
//                return;
//            }
//
//            // ⭐ 6. 파일 읽기 및 복호화 처리
//            byte[] fileDataBytes;
//
//            if (isEncrypted) {
//                log.info("암호화된 파일 복호화 시작");
//                // 암호화된 파일 읽기
//                byte[] encryptedData = Files.readAllBytes(filePath);
//                log.info("암호화 데이터 크기: {} bytes", encryptedData.length);
//
//                // 복호화
//                fileDataBytes = cryptoService.decrypt(encryptedData);
//                log.info("복호화 완료 - 크기: {} bytes", fileDataBytes.length);
//            } else {
//                log.info("일반 파일 읽기");
//                fileDataBytes = Files.readAllBytes(filePath);
//                log.info("파일 크기: {} bytes", fileDataBytes.length);
//            }
//
//            // 7. Content-Type 설정
//            String contentType = Files.probeContentType(filePath);
//            if (contentType == null) {
//                contentType = "application/octet-stream";
//            }
//            response.setContentType(contentType);
//
//            // 8. 파일명 인코딩 처리 (한글 지원)
//            String downloadFilename = originalFilename != null ? originalFilename : filename;
//            String encodedFilename = URLEncoder.encode(downloadFilename, StandardCharsets.UTF_8.toString())
//                    .replaceAll("\\+", "%20");
//
//            // 9. 응답 헤더 설정
//            String contentDisposition = String.format(
//                    "attachment; filename=\"%s\"; filename*=UTF-8''%s",
//                    encodedFilename,
//                    encodedFilename
//            );
//            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
//            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileDataBytes.length));
//
//            // 10. 데이터 전송 (복호화된 데이터)
//            outputStream = response.getOutputStream();
//            outputStream.write(fileDataBytes);
//            outputStream.flush();
//
//            log.info("다운로드 완료 - file: {}, size: {} bytes, encrypted: {}",
//                    downloadFilename, fileDataBytes.length, isEncrypted);
//
//        } catch (Exception e) {
//            log.error("File download error: ", e);
//            try {
//                if (!response.isCommitted()) {
//                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//                            "File download failed: " + e.getMessage());
//                }
//            } catch (IOException ex) {
//                log.error("Error sending error response", ex);
//            }
//        } finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    log.error("Error closing output stream", e);
//                }
//            }
//        }
//    }


//    @GetMapping("/download-stream")
//    public void downloadStream(@RequestParam String filename, HttpServletResponse response) {
//        InputStream inputStream = null;
//        OutputStream outputStream = null;
//
//        try {
//            // 1. 파일명 검증
//            if (!isValidFilename(filename)) {
//                log.error("Invalid filename detected: {}", filename);
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid filename");
//                return;
//            }
//
//            // 인코딩 파일명으로 DB에서 파일 정보 조회
//            FileDataReqVO fileParam = new FileDataReqVO();
//            fileParam.setSrvrFileNm(filename);
//            FileDataResVO fileData = fileMapper.data(fileParam);
//
//            if (fileData == null) {
//                log.error("File data not found in DB: {}", filename);
//                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File data not found");
//                return;
//            }
//
//            String fileurl = fileData.getFileStrgPathDsctn(); // "C:\\data\\attach\\202601\\ca";
//            String originalFilename = fileData.getFileNm(); // 원본 파일명 (한글 가능)
//
//            log.info("File download request - stored: {}, original: {}, path: {}",
//                    filename, originalFilename, fileStorePath + fileurl);
//
//            // 2. 파일 경로 생성
//            Path filePath = Paths.get(fileStorePath + fileurl).resolve(filename).normalize(); // fileStorePath
//
//            File file = filePath.toFile();
//
//            // 3. 파일 존재 여부 확인
//            if (!file.exists() || !file.isFile()) {
//                log.error("File not found: {}", filePath);
//                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
//                return;
//            }
//
//            // 4. 보안 검사
//            if (!filePath.startsWith(Paths.get(fileStorePath).normalize())) {
//                log.error("Access denied - file outside allowed directory: {}", filePath);
//                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
//                return;
//            }
//
//            // 5. Content-Type 설정
//            String contentType = Files.probeContentType(filePath);
//            if (contentType == null) {
//                contentType = "application/octet-stream";
//            }
//            response.setContentType(contentType);
//
//            // 6. 원본 파일명 인코딩 처리 (한글 지원)
//            String downloadFilename = originalFilename != null ? originalFilename : filename;
//            String encodedFilename = URLEncoder.encode(downloadFilename, StandardCharsets.UTF_8.toString())
//                    .replaceAll("\\+", "%20");
//
//            // 7. 응답 헤더 설정 (RFC 5987 표준 - 한글 파일명 지원)
//            // filename*=UTF-8''encoded-filename (모든 모던 브라우저)
//            // filename="encoded-filename" (IE용 fallback)
//            String contentDisposition = String.format(
//                    "attachment; filename=\"%s\"; filename*=UTF-8''%s",
//                    encodedFilename,  // IE용 (fallback)
//                    encodedFilename   // 모던 브라우저용 (UTF-8 지원)
//            );
//            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
//            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
//
//            // 8. 파일 스트리밍
//            inputStream = new BufferedInputStream(new FileInputStream(file));
//            outputStream = new BufferedOutputStream(response.getOutputStream());
//
//            byte[] buffer = new byte[8192]; // 8KB 버퍼
//            int bytesRead;
//
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//
//            outputStream.flush();
//            log.info("File download completed - original: {}, stored: {}, size: {} bytes",
//                    downloadFilename, filename, file.length());
//
//        } catch (IOException e) {
//            log.error("File download error: {}", e.getMessage(), e);
//            try {
//                if (!response.isCommitted()) {
//                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//                            "File download failed");
//                }
//            } catch (IOException ex) {
//                log.error("Error sending error response", ex);
//            }
//        } finally {
//            // 9. 리소스 정리
//            try {
//                if (inputStream != null) inputStream.close();
//                if (outputStream != null) outputStream.close();
//            } catch (IOException e) {
//                log.error("Error closing streams", e);
//            }
//        }
//    }
//

    /**
     * 파일 존재 여부 확인 API
     * @param filename 확인할 파일명
     * @return 존재 여부
     */
    private boolean isValidFilename(String filename) {
        if (!StringUtils.hasText(filename)) {
            return false;
        }

        // Path Traversal 공격만 차단 (..만 차단)
        if (filename.contains("..") || filename.contains("\0")) {
            return false;
        }

        // 정상적인 경로 구분자는 허용, 파일명 패턴 검증
        // Windows: \, Linux/Mac: /
        String normalizedPath = filename.replace("\\", "/");

        // 각 경로 구성 요소 검증 (빈 값, 특수문자 등)
        String[] parts = normalizedPath.split("/");
        for (String part : parts) {
            if (part.isEmpty() || !part.matches("^[a-zA-Z0-9가-힣._\\-\\s()]+$")) {
                return false;
            }
        }

        return true;
    }


    @GetMapping("/exists")
    public ResponseEntity<Boolean> fileExists(@RequestParam String filename) {
        try {
            if (!isValidFilename(filename)) {
                return ResponseEntity.badRequest().body(false);
            }

            Path filePath = Paths.get(fileStorePath).resolve(filename).normalize();
            File file = filePath.toFile();

            boolean exists = file.exists() && file.isFile() &&
                    filePath.startsWith(Paths.get(fileStorePath).normalize());

            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            log.error("Error checking file existence: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    /**
     * 개인정보포함된 파일을 관리자가 다운로드 할 시 사유입력 (ResponseEntity 방식)
     * @param filename 다운로드할 파일명
     *        reason   다운로드 사유
     * @return ResponseEntity<Resource>
     */
    @Operation( summary = "파일 다운로드 사유 입력", description = "파일 다운로드시 사유를 입력합니다.")
    @ApiResponse(responseCode = "200", description = "파일 다운로드 사유 입력 성공")
    @PostMapping("/download-reason")
    public ResponseEntity<Resource> downloadWithReason(@RequestParam String filename,@RequestParam(required = false) String reason,
                                                       HttpServletRequest request) {

        try {
            log.info("파일 다운로드 요청 - filename: {}, reason: {}", filename, reason);
            // 1. 개인정보 포함 파일 여부 확인 - 파일정보에 업무에서 보는 개인정보 여부로 검증
            FileMetaVO fileMetaData = fileMapper.selectFileMetaByFilename(filename);
            boolean hasPrivacyInfo = checkPrivacyInfo(fileMetaData);

            // 2. 개인정보 파일은 사유 필수
            if (hasPrivacyInfo) {
                if (reason == null || reason.trim().isEmpty()) {
                    log.warn("개인정보 파일 사유 미입력 - filename: {}", filename);
                    return ResponseEntity.badRequest().build(); // 400 Bad Request
                }

                // 2-1. 사유 최소 길이 체크
                if (reason.trim().length() < 1) {
                    log.warn("개인정보 파일 사유 부족 - filename: {}, length: {}",
                            filename, reason.trim().length());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                // ✅ 로그인 ID 조회
                String loginId = (String) request.getSession().getAttribute("LOGIN_ID");
                // 2-2. 사유 입력시 로그 테이블에 적재
                FileDownloadLogReqVO logVO = new FileDownloadLogReqVO();
                logVO.setAcsrId(loginId);
                logVO.setTrgtMenuNm("파일다운로드");
                logVO.setQna(reason);
                logVO.setUrlAddr("/api/file/download");
                logVO.setPrvcInclYn("Y");

                fileService.saveDownloadLog(logVO);
                log.warn("개인정보 파일 다운로드 - filename: {}, reason: {}", filename, reason);
            }

            // 3. 파일 경로 생성
            Path filePath = Paths.get(fileStorePath).resolve(filename).normalize();
            File file = filePath.toFile();

            // 4. 파일 존재 여부 확인
            if (!file.exists() || !file.isFile()) {
                log.error("File not found: {}", filePath);
                return ResponseEntity.notFound().build();
            }

            // 5. 보안: 파일이 지정된 디렉토리 내에 있는지 확인 (Path Traversal 방지)
            if (!filePath.startsWith(Paths.get(fileStorePath).normalize())) {
                log.error("Access denied - file outside allowed directory: {}", filePath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // 6. 파일을 Resource로 변환
            Resource resource = new FileSystemResource(file);

            // 7. Content-Type 자동 감지
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 8. 한글 파일명 인코딩 처리 (RFC 5987 표준)
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");

            // 9. 응답 헤더 설정 (모든 브라우저 호환)
            HttpHeaders headers = new HttpHeaders();
            
            // RFC 5987: filename*=UTF-8''encoded-filename (IE 제외 모든 모던 브라우저)
            // filename="original-filename" (IE 지원용)
            String contentDisposition = String.format(
                "attachment; filename=\"%s\"; filename*=UTF-8''%s",
                encodedFilename,  // IE용 (fallback)
                encodedFilename   // 모던 브라우저용 (UTF-8 지원)
            );
            headers.set(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));

            log.info("File download started: {}, size: {} bytes", filename, file.length());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            log.error("File download error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 각 업무단의 개인정보 포함 여부 확인 (PrvcInclYn : 1 일 경우)
     */
    public boolean checkPrivacyInfo(FileMetaVO fileMetaData) {
        return fileMetaData != null
                && "1".equals(fileMetaData.getPrvcInclYn());
    }
}



