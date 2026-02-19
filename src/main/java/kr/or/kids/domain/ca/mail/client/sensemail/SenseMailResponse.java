package kr.or.kids.domain.ca.mail.client.sensemail;

import lombok.Data;

@Data
public class SenseMailResponse {
    private String resultCode;
    private String messageId;
    private String errorMessage;
}
