package kr.or.kids.domain.ca.mail.mapper;

import kr.or.kids.domain.ca.mail.vo.EmlSndngHistVO;
import kr.or.kids.domain.ca.mail.vo.MailListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmlSndngHistMapper {

    /** 메일 발송 이력 insert */
    public int insertHist(EmlSndngHistVO vo);

    /** 메일 발송 이력 리스트 조회 */
    public List<MailListVO> selectMailSendList(MailListVO mailList);
}
