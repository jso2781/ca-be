package kr.or.kids.domain.ca.mail.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
public class EmlSndngVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 메일 발송 일련번호 (PK) */
    private BigInteger emlSndngSn;

    /** 메일 제목 */
    private String emlTtl;

    /** 메일 내용 */
    private String emlCn;

    /** 발송자 이름 */
    private String sndptyFlnm;

    /** 발송자 이메일 */
    private String sndptyEmlAddr;

    /** 수신자 이름 */
    private String rcvrFlnm;

    /** 수신자 이메일 */
    private String rcvrEmlAddr;

    /** 수신자 직급명 */
    private String rcvrJbpsNm;

    /** 발송 결과 코드 (READY/SUCCESS/FAIL) */
    private String sndngRsltCd;

    /** 첨부파일 ID */
    private String atchFileId;

    /** 발송 일시 */
    private LocalDateTime sndngDttm;

    /** 센스메일 요청 ID */
    private String extReqId;

    /** 등록일시 */
    private LocalDateTime regDt;

    /** 등록자 ID */
    private String rgtrId;

    /** 수정일시 */
    private LocalDateTime mdfcnDt;

    /** 수정자 ID */
    private String mdfrId;
}
