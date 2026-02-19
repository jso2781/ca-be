package kr.or.kids.domain.ca.connecionlog.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class WorkAccessLogDataReqVO {

    private String prvcHstryMdfcnSn;   // 접속자아이디
    private BigDecimal cntnLogSn;      // 접속로그일련번호(PK)
    private Timestamp inptDt;          // 입력일시
    private String instCd;             // 기관코드
    private String trgtMenuNm;         // 대상메뉴명
    private String qnaSqlCn;           // 질의응답쿼리 (TEXT)
    private String urlAddr;            // URL주소
    private String taskSeCdNo;         // 업무구분코드번호
    private Timestamp cntnDt;          // 접속일시
    private String acsrNm;             // 접속자명
    private String rqstrId;            // 요청자아이디
    private String taskSeCd;           // 업무구분코드
    private String flfmtTaskCd;        // 세부업무코드
    private String srvcNm;             // 서비스명
    private String etcMemoCn;          // 기타메모
    private String prvcInclYn;         // 개인정보포함여부 (Y/N)
    private Timestamp regDt;           // 등록일시
    private String rgtrId;             // 등록자아이디

}
