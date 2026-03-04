package kr.or.kids.domain.ca.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.or.kids.domain.ca.common.file.service.FileService;
import kr.or.kids.domain.ca.common.file.vo.FileDataReqVO;
import kr.or.kids.domain.ca.common.file.vo.FileDeleteReqVO;
import kr.or.kids.domain.ca.common.file.vo.FileGroupInsertReq;
import kr.or.kids.domain.ca.common.file.vo.FileGroupReqData;
import kr.or.kids.global.system.common.ApiResultCode;
import kr.or.kids.global.system.common.CommonController;
import kr.or.kids.global.system.common.vo.ApiPrnDto;
import kr.or.kids.global.system.common.vo.PageRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/ca/file")
public class FileController extends CommonController {

    @Autowired
    private FileService fileService;

    /**
     * 파일목록
     * @param uploadFiles 업로드할 파일들
     * @param request HTTP 요청
     * @return API 응답
     */
    @Operation( summary = "파일 목록 조회", description = "파일 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "파일목록 조회 성공")
    @PostMapping("/list")
    public ResponseEntity<ApiPrnDto> fileList(@RequestBody FileDataReqVO param, HttpServletRequest request, PageRequestDto pageRequestDto) {

        int pageNum = pageRequestDto != null ? pageRequestDto.getPageNum() : 1;
        int pageSize = pageRequestDto != null ? pageRequestDto.getPageSize() : 10;

        ApiPrnDto apiPrnDto = fileService.list(param,pageNum, pageSize);
        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }


    /**
     * 파일 업로드
     * @param uploadFiles 업로드할 파일들
     * @param request HTTP 요청
     * @return API 응답
     */
    @Operation( summary = "파일 등록", description = "파일을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "파일등록  성공")
    @PostMapping("/uploadFiles")
    public ResponseEntity<ApiPrnDto> uploadFiles(@RequestParam(value="uploadFiles") MultipartFile[] uploadFiles, HttpServletRequest request) {

        /**  파일경로 정보 */
        String savePath = request.getParameter("savePath");
        /**  파일그룹 일련번호  */
        String atchFileGroupId = request.getParameter("atchFileGroupId");
        /**  개인정보 여부  */
        String prvcInclYn = request.getParameter("prvcInclYn");
        /**  엑셀파일 여부  */
        String isExcel = request.getParameter("isExcel");

        HashMap<String, Object> params = new HashMap<>();
        params.put("savePath",savePath);
        params.put("atchFileGroupId",atchFileGroupId);
        params.put("prvcInclYn",prvcInclYn);
        params.put("isExcel",isExcel);

        ApiPrnDto apiPrnDto = fileService.uploadFiles(params, uploadFiles);
        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }

    /**
     * 공통파일 정보등록 API
     * @param input
     * @return
     */
    @Operation( summary = "파일 개별 삭제", description = " 파일 개별정보를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "파일 개별정보를 삭제  성공")
    @PostMapping("/deleteFileOne")
    public ResponseEntity<ApiPrnDto> deleteFileOne(@RequestBody FileDeleteReqVO param, HttpServletRequest request) {
        ApiPrnDto apiPrnDto = fileService.deleteFileOne(param);
        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }

    /**
     * 공통파일 정보등록 API
     * @param input
     * @return
     */
    @Operation( summary = "파일 개별 삭제", description = " 파일 개별정보를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "파일 개별정보를 삭제  성공")
    @PostMapping("/deleteMultiFile")
    public ResponseEntity<ApiPrnDto> deleteMultiFile(@RequestBody FileDeleteReqVO param, HttpServletRequest request) {

        ApiPrnDto apiPrnDto =  fileService.deleteMultiFile(param);
        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }


    /**
     * 공통파일 정보등록 API
     * @param input
     * @return
     */
    @Operation( summary = "파일 그룹단위 삭제", description = " 파일정보 그룹단위를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "그룹단위 파일정보 삭제  성공")
    @PostMapping("/deleteGroupFiles")
    public ResponseEntity<ApiPrnDto> deleteGroupFiles(@RequestBody FileDeleteReqVO param, HttpServletRequest request) {
        ApiPrnDto apiPrnDto = fileService.deleteGroupFiles(param);
        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }

    /**
     * 공통파일 그룹아이디 조회 API
     * @param input
     * @return
     */
    @Operation( summary = "파일 그룹SN 조회", description = "파일 그룹SN 조회합니다.")
    @ApiResponse(responseCode = "200", description = "파일 그룹SN 조회 성공")
    @PostMapping("/groupData")
    public ResponseEntity<ApiPrnDto> groupData(@RequestBody FileGroupReqData param, HttpServletRequest request) {

        ApiPrnDto apiPrnDto = fileService.groupData(param);
        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }

    /**
     * 공통파일 그룹아이디  존재 여부 조회 API
     * @param input
     * @return
     */
    @Operation( summary = "파일 그룹SN 조회", description = "파일 그룹SN 조회합니다.")
    @ApiResponse(responseCode = "200", description = "파일 그룹SN 조회 성공")
    @PostMapping("/isGroupData")
    public ResponseEntity<ApiPrnDto> isGroupData(@RequestBody FileGroupReqData param, HttpServletRequest request) {

        ApiPrnDto apiPrnDto = fileService.isGroupData(param);
        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }

    /**
     * 공통파일 정보등록 API
     * @param input
     * @return
     */
    @Operation( summary = "파일그룹 정보등록", description = "파일그룹 정보를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "파일그룹 정보 등록  성공")
    @PostMapping("/groupInsert")
    public ResponseEntity<ApiPrnDto> groupInsert(@RequestBody FileGroupInsertReq param, HttpServletRequest request) {
        ApiPrnDto apiPrnDto = fileService.groupInsert(param);
        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }
    /**
     * 공통파일 그룹정보 API
     * @param input
     * @return
     */
    @Operation( summary = "파일그룹 정보수정", description = "파일그룹 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "파일그룹 정보 수정 성공")
    @PostMapping("/groupUpdate")
    public ResponseEntity<ApiPrnDto> groupUpdate(@RequestBody FileGroupInsertReq param, HttpServletRequest request) {
        ApiPrnDto apiPrnDto = fileService.groupUpdate(param);
        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }


}
