package kr.or.kids.domain.ca.msg.mapper;

import kr.or.kids.domain.ca.msg.vo.MsgListVO;
import org.apache.ibatis.annotations.Mapper;
import kr.or.kids.domain.ca.msg.vo.MsgSndngVO;

import java.util.List;

@Mapper
public interface MsgSndngMapper {

    /**
     * 메시지 발송요청 등록
     */
    public void insertMsgSndng(MsgSndngVO msgVO);

    /** 메시지 발송내역 조회 (단일 테이블) */
    public List<MsgListVO> selectMsgList(MsgListVO msgListVO);
}
