package kr.or.kids.domain.ca.connecionlog.mapper;

import kr.or.kids.domain.ca.connecionlog.vo.WorkAccessLogDataResVO;
import kr.or.kids.domain.ca.connecionlog.vo.WorkAccessLogInsertVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WorkAccessLogMapper {

    /**  접속 내역 조회 */
    List<WorkAccessLogDataResVO> list();
    /**  접속 로그 상세  일련번호 조회 */
    public long nextConnecttionDetailId();

    /**  접속 로그 정보 단건 등록 */
    public int insert(WorkAccessLogInsertVO param);

}
