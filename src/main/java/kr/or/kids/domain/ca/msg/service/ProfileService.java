// ProfileService.java
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
public class ProfileService {

    private final SuremMessageClient client;

    public SuremBaseResponse requestProfileToken(String yellowId, String phoneNumber) {
        return client.requestProfileToken(yellowId, phoneNumber);
    }

    public SuremProfileCreateResponse createProfile(Map<String, Object> req) {
        return client.createProfile(req);
    }

    public SuremBaseResponse getProfileList() {
        return client.getProfileList();
    }

    public SuremBaseResponse getProfile(String senderKey) {
        return client.getProfile(senderKey);
    }

    public SuremBaseResponse releaseProfileDormant(String senderKey) {
        return client.releaseProfileDormant(senderKey);
    }

    public SuremBaseResponse updateUnsubscribe(String senderKey, String unsubscribePhone, String unsubscribeAuth) {
        return client.updateUnsubscribe(senderKey, unsubscribePhone, unsubscribeAuth);
    }
}