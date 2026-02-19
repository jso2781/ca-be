package kr.or.kids.domain.ca.msg.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MsgImgVO {

    private BigDecimal msgImgSn;
    private BigDecimal seqno;

    private Integer fileCnt;
    private String filePath1;
    private String filePath2;
    private String filePath3;

    private String biztype;
    private String usercode;

    private String rgtrId;
    private String mdfrId;
}
