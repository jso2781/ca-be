package kr.or.kids.domain.ca.connecionlog.vo;

import lombok.Data;

@Data
public class PersonalInfoAccessLogResVO {

    public Long prvcHstrySn;    // 개인정보 이력조회 일련번호
    public String taskSysSeCd;  // 업무시스템 구분
    public Long menuSn;         // 메뉴 일련번호
    public String cntnDt;       // 접속일시
    public String acsrInfoNm;   // 접속자정보
    public Long prcsNocs;       // 처리건수
    public String dwnldYn;      // 다운로드여부
    public String dwnldRsn;     // 다운로드이유
    public String flfmtTaskNm;  // 수행업무(CRUD)
    public String regDt;        // 등록일시
    public String rgtrId;       // 등록자ID
    public String mdfcnDt;      // 수정일시
    public String mdfrId;       // 수정자ID

}
