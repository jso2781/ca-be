package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Data;

@Data
public class SuremKakaoImageResponse {
    private String code;
    private String message;
    private String data; // 이미지 URL
}
