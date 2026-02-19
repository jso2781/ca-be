package kr.or.kids.domain.ca.msg.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MsgRsltVO {

    private BigDecimal seqno;
    private String rsltCd;
    private String rsltMsg;
    private String rgtrId;
}
