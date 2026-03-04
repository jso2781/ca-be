package kr.or.kids.domain.ca.mail.vo;

import lombok.Data;

@Data
public class MailSendReqVO {
    private String emlTtl;
    private String emlCn;

    private String sndptyFlnm;
    private String sndptyEmlAddr;

    private String rcvrFlnm;
    private String rcvrEmlAddr;

}
