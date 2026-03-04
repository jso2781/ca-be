package kr.or.kids.domain.ca.msg.client.surem.dto;
import lombok.Data;

@Data
public class SuremProfileCreateResponse {
    private String code;
    private String message;
    private ProfileData data;

    @Data
    public static class ProfileData {
        private String yellowId;
        private String senderKey;
    }
}
