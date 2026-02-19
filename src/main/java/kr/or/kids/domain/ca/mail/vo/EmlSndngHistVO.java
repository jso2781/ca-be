package kr.or.kids.domain.ca.mail.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
public class EmlSndngHistVO {
    private BigInteger emlSndngHistSn;
    private BigInteger emlSndngSn;

    private String rcvrEmlAddr;
    private String rcvrFlnm;

    private String sndngRsltCd;
    private String failRsnCn;

    private Integer sndngTryCnt;
    private String sndngDttm;

    private String extSndngRsltCd;
    private String extFailMsg;
    private String extMsgId;

    private LocalDateTime regDt;
    private String rgtrId;
}
