package kr.or.kids.domain.ca.adr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.or.kids.domain.ca.adr.service.AdressCntcService;
import kr.or.kids.domain.ca.adr.vo.JusoResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/ca/adr")
public class AdressCntcController {

    @Autowired
    private AdressCntcService adressCntcService;

    /**
     * 주소 목록 조회 (JSON 방식)
     * @param keyword      검색 키워드 (필수)
     * @param currentPage  현재 페이지 (기본값: 1)
     * @param countPerPage 페이지당 건수 (기본값: 10)
     * @return JusoResponseVO (JSON 형식)
     */
    @Operation(summary = "주소 목록 조회", description = "주소 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주소 조회 성공")
    @ApiResponse(responseCode = "400", description = "keyword 파라미터 누락 또는 빈 값")
    @GetMapping("/search/openapi")
    public ResponseEntity<JusoResponseVO> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int countPerPage
    ) throws Exception {

        // keyword 검증
        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("주소 검색 요청 실패 - keyword 파라미터 누락 또는 빈 값");
            return ResponseEntity.badRequest().build();
        }

        log.info("주소 검색 요청 - keyword: {}, currentPage: {}, countPerPage: {}",
                keyword, currentPage, countPerPage);

        JusoResponseVO result = adressCntcService.search(keyword, currentPage, countPerPage);

        return ResponseEntity.ok(result);
    }

}