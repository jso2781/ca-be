package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuremCampaignRequest {
    private String campaignName;
    private String messageType;
    private String messageSubtype;
    private String kakaoSenderKey;
    private Object previewContent;
}