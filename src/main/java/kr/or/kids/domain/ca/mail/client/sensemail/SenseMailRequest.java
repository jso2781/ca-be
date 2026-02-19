package kr.or.kids.domain.ca.mail.client.sensemail;

import kr.or.kids.domain.ca.mail.vo.MailSendReqVO;
import lombok.Data;

@Data
public class SenseMailRequest {
    private String subject;
    private String body;
    private String from;
    private String to;
    private String apiKey;

    public static SenseMailRequest from(MailSendReqVO req) {
        SenseMailRequest senseReq = new SenseMailRequest();
        senseReq.setSubject(req.getTitle());
        senseReq.setBody(req.getContent());
        senseReq.setFrom(req.getSenderEmail());
        senseReq.setTo(req.getReceiverEmail());
        return senseReq;
    }
}
