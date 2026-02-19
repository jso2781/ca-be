package kr.or.kids.domain.ca.msg.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MsgListVO {

    private Long seqNo;              // 순번
    private String status;            // 상태
    private String type;              // 유형
    private String recvPhone;         // 수신번호(마스킹)
    private String title;             // 제목
    private String message;           // 메시지내용
    private LocalDateTime sendDateTime;     // 발송일시
    private LocalDateTime receiveDateTime;  // 수신일시
}
