package kr.or.kids.domain.ca.msg.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MsgSndngVO {

    private BigDecimal seqno;
    private String usercode;
    private String biztype;
    private String kind;
    private String reqname;
    private String reqphone;
    private String callname;
    private String callphone;
    private String subject;
    private String msg;
    private String rgtrId;
    private String intime;
    private String yellowKey;
    private String reqtime;
    private String sendtime;
    private String rectime;
    private String result;
    private String errcode;
    private String fkcontent;
    private int retry;
    private String resend;
    private String mdfcnDt;
    private String mdfrId;
    private String yellowidKey;
    private String senttime;
    private String recvtime;
    private String sentmedia;
    private String kakaoerrcode;

    // 이미지
    private String etc1;
    private String etc2;
    private String etc3;
    private String etc4;
    private String etc5;

    // 국제문자
    private String country;

    // 카카오
    private String senderKey;
    private String templatecode;
    private String reSend;
    private String reSubject;
    private String retext;
    private String sendmedia;
    private String mediatype;
    private String attachment;
    private String supplement;
    private String carousel;
    private String header;
    private String kakaorecord;
    private String templateTitle;
    private String targeting;
    private String campaignname;
    private String origCode;
    private String processid;

    // RCS
    private String serviceType;
    private String brandKey;
    private String chatbotId;
    private String messagebaseId;
    private Object rcsBody;
}