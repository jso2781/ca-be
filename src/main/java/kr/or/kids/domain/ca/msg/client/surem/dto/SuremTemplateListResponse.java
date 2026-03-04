package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class SuremTemplateListResponse {
    private String code;
    private String message;
    private ListData data;

    @Data
    public static class ListData {
        private List<Map<String, String>> list;
    }
}
