package kr.or.kids.domain.ca.mail.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
public class EmlSndngHistVO {
    private BigInteger emlSndngHstrySn;
    private BigInteger emlSndngSn;

    private String rcvrEmlAddr;
    private String rcvrFlnm;

    private String sndngRsltCd;
    private String failRsn;
    private String rsndNmtm;

    private Integer sndngTryCnt;
    private String dsptchDt;

    private String otsdEmlOrgnlRsltCd;
    private String otsdEmlErrMsgCn;
    private String otsdEmlMsgId;

    private LocalDateTime regDt;
    private String rgtrId;
    private LocalDateTime mdfcnDt;
    private String mdfrId;
}
