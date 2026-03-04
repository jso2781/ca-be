package kr.or.kids.domain.ca.msg.service;

import kr.or.kids.domain.ca.msg.client.surem.SuremMessageClient;
import kr.or.kids.domain.ca.msg.client.surem.dto.SuremCampaignRequest;
import kr.or.kids.domain.ca.msg.client.surem.dto.SuremCampaignResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignService {

    private final SuremMessageClient client;

    public SuremCampaignResponse createCampaign(SuremCampaignRequest req) {
        return client.createCampaign(req);
    }
}