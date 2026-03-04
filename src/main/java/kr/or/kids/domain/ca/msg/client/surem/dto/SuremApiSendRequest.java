package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuremApiSendRequest {

    // ========== 공통 ==========
    private String to;
    private String text;
    private String reqPhone;
    private String subject;
    private String reservedTime;
    private Integer messageId;
    private String imageKey;
    private String origCode;

    // ========== 국제문자 ==========
    private String country;

    // ========== 카카오 ==========
    private String bizType;
    private String senderKey;
    private String templateCode;
    private String templateTitle;
    private String reSend;
    private String reSubject;
    private String reText;
    private Object attachment;
    private Object supplement;
    private String header;
    private Object carousel;

    // ========== RCS ==========
    private String serviceType;
    private String brandKey;
    private String chatbotId;
    private String phone;
    private String messagebaseId;
    private Object body;
    private Object buttons;
    private String footer;
    private Boolean copyAllowed;
}