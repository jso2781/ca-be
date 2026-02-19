package kr.or.kids.domain.ca.mail.vo;

import lombok.Data;

@Data
public class MailSendReqVO {
    private String title;
    private String content;

    private String senderName;
    private String senderEmail;

    private String receiverName;
    private String receiverEmail;

}
