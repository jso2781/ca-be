package kr.or.kids.domain.ca.msg.mapper;

import org.apache.ibatis.annotations.Mapper;
import kr.or.kids.domain.ca.msg.vo.MsgRsltVO;

@Mapper
public interface MsgRsltMapper {

    /**
     * 메시지 발송결과 등록
     */
    public void insertMsgRslt(MsgRsltVO msgVO);
}
