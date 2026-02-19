package kr.or.kids.domain.ca.adr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.or.kids.domain.ca.adr.service.AdressCntcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/ca/adr")
public class AdressCntcController {
    @Autowired
    private AdressCntcService adressCntcService;
    /**
     * 주소 목록
     * @param  pageRequestDto 페이지용 데이터
     * @param request HTTP 요청
     * @return API 응답
     */
    @Operation( summary = "주소 목록 조회", description = "주소 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주소 조회 성공")
    @GetMapping("/search/openapi")
    public void search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int countPerPage,
            HttpServletResponse response
    ) throws Exception {

        String xml = adressCntcService.search(keyword, currentPage, countPerPage);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml;charset=UTF-8");
        response.getWriter().write(xml);
    }

}
