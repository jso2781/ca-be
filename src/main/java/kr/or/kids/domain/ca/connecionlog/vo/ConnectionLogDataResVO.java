package kr.or.kids.domain.ca.connecionlog.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ConnectionLogDataResVO {

    private Long sessLogSn;                 // 접속로그일련번호(PK)
    private String lgnSeCd;                 // 외내부망구분
    private String srvcUserId;              // 서비스사용자아이디
    private String netSeCd;                 // 망구분 1내부(관리자)2 외부사용자
    private String rqstrIpAddr;             // 요청자IP주소
    private String cntnSeNo;                // 접속구분번호
    private String cntnOcrnDt;              // 접속발생일시 (CHAR(14))
    private String cntnDtlExpln;            // 접속상세설명
    private String certTokenVl;             // 인증토큰값
    private String tokenCrtHr;              // 토큰생성시간 (CHAR(6))
    private String sessExpryPrnmntHr;       // 세션만료예정시간 (CHAR(6))
    private String taskSeCd;                // 업무구분코드 (CHAR(6))
    private Timestamp lgtDt;                // 로그아웃일시
    private Timestamp regDt;                // 등록일시
    private String rqtrId;                  // 등록자아이디
    private Timestamp mdfcnDt;              // 수정일시
    private String  mdfrId;                 // 수정자아이디

}
