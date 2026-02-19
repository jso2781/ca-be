package kr.or.kids.domain.ca.msg.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MsgSndngVO {

    /** PK */
    private BigDecimal seqno;

    /** 입력일시 (yyyyMMddHHmmss) */
    private String intime;

    /** 사용자코드 */
    private String usercode;

    /** 업무구분 */
    private String biztype;

    /** 옐로우키 */
    private String yellowKey;

    /** 요청자명 */
    private String reqname;

    /** 수신번호 (NOT NULL) */
    private String reqphone;

    /** 발신자명 */
    private String callname;

    /** 국가코드 */
    private String country;

    /** 발신번호 (NOT NULL) */
    private String callphone;

    /** 제목 (LMS) */
    private String subject;

    /** 메시지 내용 (NOT NULL) */
    private String msg;

    /** 요청시간 */
    private String reqtime;

    /** 발송시간(예약) */
    private String sendtime;

    /** 수신시간 */
    private String rectime;

    /** 결과 (Y/N/F) */
    private String result;

    /** 오류코드 */
    private BigDecimal errcode;

    /** 메시지종류 */
    private String kind;

    /** 첨부개수 */
    private BigDecimal fkcontent;

    /** 재시도횟수 */
    private BigDecimal retry;

    /** 재발송여부 */
    private String resend;

    /** 템플릿코드 */
    private String templatecode;

    /** 대체문구 */
    private String retext;

    /** 발송매체 */
    private String sendmedia;

    /** 미디어타입 */
    private String mediatype;

    /** 첨부정보 */
    private String attachment;

    /** 보조정보 */
    private String supplement;

    /** 캐러셀 */
    private String carousel;

    /** 헤더 */
    private String header;

    /** 카카오코드 */
    private String kakaorecord;

    /** 템플릿제목 */
    private String templateTitle;

    /** 타겟팅 */
    private String targeting;

    /** 캠페인명 */
    private String campaignname;

    /** 원발신코드 */
    private String origCode;

    /** 프로세스ID */
    private String processid;

    /** 기타 */
    private String etc1;
    private String etc2;
    private String etc3;
    private String etc4;
    private String etc5;

    /** 등록일시 */
    private LocalDateTime regDt;

    /** 등록자 */
    private String rgtrId;

    /** 수정일시 */
    private LocalDateTime mdfcnDt;

    /** 수정자 */
    private String mdfrId;
}
