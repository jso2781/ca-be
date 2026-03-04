// TemplateService.java
package kr.or.kids.domain.ca.msg.service;

import kr.or.kids.domain.ca.msg.client.surem.SuremMessageClient;
import kr.or.kids.domain.ca.msg.client.surem.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService {

    private final SuremMessageClient client;

    public SuremBaseResponse createTemplate(Map<String, Object> req) {
        return client.createTemplate(req);
    }

    public SuremTemplateResponse getTemplate(String senderKey, String templateCode) {
        return client.getTemplate(senderKey, templateCode);
    }

    public SuremTemplateListResponse getTemplateList(String senderKey, int page, int count) {
        return client.getTemplateList(senderKey, page, count);
    }

    public SuremBaseResponse updateTemplate(Map<String, Object> req) {
        return client.updateTemplate(req);
    }

    public SuremBaseResponse requestTemplate(String senderKey, String templateCode) {
        return client.requestTemplate(senderKey, templateCode);
    }

    public SuremBaseResponse cancelTemplateRequest(String senderKey, String templateCode) {
        return client.cancelTemplateRequest(senderKey, templateCode);
    }

    public SuremBaseResponse deleteTemplate(String senderKey, String templateCode) {
        return client.deleteTemplate(senderKey, templateCode);
    }

    public SuremBaseResponse releaseTemplateDormant(String senderKey, String templateCode) {
        return client.releaseTemplateDormant(senderKey, templateCode);
    }

    public SuremBaseResponse getTemplateCategory() {
        return client.getTemplateCategory();
    }
}