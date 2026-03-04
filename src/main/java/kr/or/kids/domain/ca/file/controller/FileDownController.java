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
     * 파일 다운로드
     * GET /api/file/downloadStream?filename=저장파일명
     */
    @GetMapping("/downloadStream")
    public void downloadStream(
            @RequestParam String filename,
            HttpServletResponse response) {
        fileService.downloadStream(filename, response);
    }

    /**
     * 파일 다운로드
     * GET /api/file/downloadfilename=저장파일명
     */
    @GetMapping("/download")
    public void download(
            @RequestParam String filename,
            HttpServletResponse response) {
        fileService.downloadStream(filename, response);
    }

    /**
     * 각 업무단의 개인정보 포함 여부 확인 (PrvcInclYn : 1 일 경우)
     */
    public boolean checkPrivacyInfo(FileMetaVO fileMetaData) {
        return fileMetaData != null
                && "1".equals(fileMetaData.getPrvcInclYn());
    }
}



