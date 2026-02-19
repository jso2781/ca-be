package kr.or.kids.domain.ca.mail.vo;

import lombok.Data;

@Data
public class MailListVO {

    private Long seqNo;              // 순번 (이력 일련번호)
    private String status;            // 상태 (S / F)
    private String receiver;          // 수신자 (이름<이메일>)
    private String subject;           // 제목
    private String sendDateTime;       // 메일발송일 (YYYYMMDDHH24MISS)
    private String reason;             // 사유 (실패사유)
}
