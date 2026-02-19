package kr.or.kids.domain.ca.mail.mapper;

import kr.or.kids.domain.ca.mail.vo.EmlSndngVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmlSndngMapper {

    public int insertEmlSndng(EmlSndngVO vo);

    public int updateResult(EmlSndngVO vo);

}
