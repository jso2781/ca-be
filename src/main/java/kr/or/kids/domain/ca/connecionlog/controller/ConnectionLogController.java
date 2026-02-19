package kr.or.kids.domain.ca.connecionlog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.or.kids.domain.ca.connecionlog.service.ConnectionLogService;
import kr.or.kids.domain.ca.connecionlog.vo.ConnectionLogInsertReqVO;
import kr.or.kids.global.system.common.ApiResultCode;
import kr.or.kids.global.system.common.vo.ApiPrnDto;
import kr.or.kids.global.system.common.vo.PageRequestDto;
import kr.or.kids.global.util.DrugsafeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/api/ca/connectionlog")
public class ConnectionLogController {

    @Autowired
    private ConnectionLogService connectionLogService;

    /**
     * 로그인접속정보 목록
     * @param  pageRequestDto 페이지용 데이터
     * @param request HTTP 요청
     * @return API 응답
     */
    @Operation( summary = "로그인 접속 내역 목록 조회", description = "로그인 접속 내역 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 접속 내역 성공")
    @PostMapping("/list")
    public ResponseEntity<ApiPrnDto> list(@RequestBody PageRequestDto pageRequestDto, HttpServletRequest request) {

        int pageNum = pageRequestDto != null ? pageRequestDto.getPageNum() : 1;
        int pageSize = pageRequestDto != null ? pageRequestDto.getPageSize() : 10;

        ApiPrnDto apiPrnDto = connectionLogService.list(pageNum, pageSize);
        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }

    /**
     * 로그인접속정보 등록
     * @param input
     * @return
     */
    @PostMapping("/insert")
    public ResponseEntity<ApiPrnDto> insert(@RequestBody ConnectionLogInsertReqVO input, HttpServletRequest request) {

        DrugsafeUtil  util = new DrugsafeUtil();
        String clientIp = util.getClientIp(request);

        input.setRqstrIpAddr(clientIp);
        ApiPrnDto apiPrnDto = connectionLogService.insert(input);

        ApiResultCode resultCode = ApiResultCode.fromCode(apiPrnDto.getCode());
        return ResponseEntity.status(resultCode.getHttpStatus()).body(apiPrnDto);
    }

}
