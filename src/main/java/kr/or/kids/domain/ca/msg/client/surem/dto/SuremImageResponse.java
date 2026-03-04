package kr.or.kids.domain.ca.msg.client.surem.dto;
import lombok.Data;

@Data
public class SuremImageResponse {
    private String code;
    private String message;
    private ImageData data;

    @Data
    public static class ImageData {
        private String imageKey;   // MMS용
        private String fileName;   // RCS용
        private String fileId;     // RCS용
        private String expiryDate;
    }
}