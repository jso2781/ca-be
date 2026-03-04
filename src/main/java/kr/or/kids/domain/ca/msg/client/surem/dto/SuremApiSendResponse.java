package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Data;

/**
 * Surem 메시지 발송 응답 DTO
 */
@Data
public class SuremApiSendResponse {

    private String code;      // 호출 결과 코드
    private String message;   // 오류 메시지
}
