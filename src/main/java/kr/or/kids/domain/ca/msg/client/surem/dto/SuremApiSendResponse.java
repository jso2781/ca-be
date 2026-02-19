package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Data;

/**
 * Surem 메시지 발송 응답 DTO
 */
@Data
public class SuremApiSendResponse {

    /** 결과 코드 (성공/실패) */
    private String result;

    /** 에러 메시지 */
    private String errmsg;
}
