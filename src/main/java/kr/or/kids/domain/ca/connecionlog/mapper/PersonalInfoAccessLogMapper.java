package kr.or.kids.domain.ca.connecionlog.mapper;

import kr.or.kids.domain.ca.connecionlog.vo.PersonalInfoAccessLogReqVO;
import kr.or.kids.domain.ca.connecionlog.vo.PersonalInfoAccessLogResVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PersonalInfoAccessLogMapper {

    /**  개인정보 조회이력  조회 */
    List<PersonalInfoAccessLogResVO> list();
    /**  개인정보 조회이력  일련번호 조회 */
    public long nextPersonalInfoAccessLogId();
    /**  개인정보 조회이력  단건 등록 */
    public int insert(PersonalInfoAccessLogReqVO req);

}
