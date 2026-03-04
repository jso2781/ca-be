package kr.or.kids.domain.ca.mail.client.sensemail;

import lombok.Data;

import java.util.HashMap;

@Data
public class SenseMailResponse {
    private String code;
    private String msg;
    private String errMsg;
    private HashMap<String, Object> data = new HashMap<>();
}
