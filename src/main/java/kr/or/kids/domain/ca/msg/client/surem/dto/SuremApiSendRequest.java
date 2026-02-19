package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Data;

/**
 * Surem 메시지 발송 요청 DTO
 */
@Data
public class SuremApiSendRequest {

    /** 사용자 코드 */
    private String usercode;

    /** 업무 타입 */
    private String biztype;

    /** 요청자명 */
    private String reqname;

    /** 요청자 전화번호 */
    private String reqphone;

    /** 발신자명 */
    private String callname;

    /** 발신자 전화번호 */
    private String callphone;

    /** 제목 (LMS/MMS) */
    private String subject;

    /** 메시지 본문 */
    private String msg;

    /** 발송 구분 (SMS, LMS, KAKAO, RCS 등) */
    private String kind;

    /** 예약 발송 시간 (yyyyMMddHHmmss) */
    private String sendtime;
}
