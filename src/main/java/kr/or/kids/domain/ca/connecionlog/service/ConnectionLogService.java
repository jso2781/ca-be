package kr.or.kids.domain.ca.connecionlog.service;

import kr.or.kids.domain.ca.connecionlog.vo.ConnectionLogInsertReqVO;
import kr.or.kids.global.system.common.vo.ApiPrnDto;


public interface ConnectionLogService {


    /**
     * 접속 내역  목록
     * @return API 응답 DTO
     */
    ApiPrnDto list(int pageNum,int  pageSize);
    /**

    /**
     * 접속로그 단건 등록
     * @return API 응답 DTO
     */
    ApiPrnDto insert(ConnectionLogInsertReqVO paramVO);

}
