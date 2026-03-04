package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Data;
import java.util.List;

@Data
public class SuremOptOutResponse {
    private String code;
    private String message;
    private List<OptOutData> data;

    @Data
    public static class OptOutData {
        private Integer id;
        private String phoneNumber;
        private String optOutStatus;     // 1: 등록, 2: 해제
        private String optOutNumber080;
        private String registeredAt;
    }
}