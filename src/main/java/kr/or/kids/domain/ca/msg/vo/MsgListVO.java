package kr.or.kids.domain.ca.msg.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MsgListVO {

    private BigDecimal seqNo;              // 순번
    private String status;            // 상태
    private String type;              // 유형
    private String recvPhone;         // 수신번호(마스킹)
    private String title;             // 제목
    private String message;           // 메시지내용
    private String sendDateTime;     // 발송일시
    private String receiveDateTime;  // 수신일시
}
