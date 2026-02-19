package kr.or.kids.domain.ca.msg.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.math.BigDecimal;
import kr.or.kids.domain.ca.msg.vo.MsgImgVO;

@Mapper
public interface MsgImgMapper {

    public BigDecimal selectNextMsgImgSn();

    public void insertMsgImg(MsgImgVO msgVO);
}
