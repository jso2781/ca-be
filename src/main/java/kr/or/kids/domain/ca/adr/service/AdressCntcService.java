package kr.or.kids.domain.ca.adr.service;

import kr.or.kids.domain.ca.adr.vo.JusoResponseVO;

public interface AdressCntcService {
    /**
     * 주소 검색 (JSON 방식)
     * @param keyword      검색 키워드
     * @param currentPage  현재 페이지
     * @param countPerPage 페이지당 건수
     * @return JusoResponseVO (JSON 응답)
     */
    JusoResponseVO search(String keyword, int currentPage, int countPerPage) throws Exception;
}