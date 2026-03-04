package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Data;

@Data
public class SuremBaseResponse {
    private String code;
    private String message;
    private Object data;
}