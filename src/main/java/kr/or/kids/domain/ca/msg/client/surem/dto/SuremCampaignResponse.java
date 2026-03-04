package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Data;

@Data
public class SuremCampaignResponse {
    private String code;
    private String message;
    private CampaignData data;

    @Data
    public static class CampaignData {
        private String campaignName;
        private String campaignKey;
    }
}