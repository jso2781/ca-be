package kr.or.kids.domain.ca.adr.service;

public interface AdressCntcService {

    /**
     * 도로명주소 Open API 기반 주소 목록 조회
     *
     * @param pageNum  페이지 번호
     * @param pageSize 페이지 사이즈
     * @return ApiPrnDto
     */
    String search(String keyword, int pageNum, int pageSize);
}
