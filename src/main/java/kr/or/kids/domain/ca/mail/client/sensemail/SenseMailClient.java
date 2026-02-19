package kr.or.kids.domain.ca.mail.client.sensemail;

import kr.or.kids.global.config.SenseMailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SenseMailClient {

    @Autowired
    public RestTemplate restTemplate;
    @Autowired
    public SenseMailProperties senseMailProperties;

    public SenseMailResponse send(SenseMailRequest req) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", senseMailProperties.getApiKey());

        HttpEntity<SenseMailRequest> entity =
                new HttpEntity<>(req, headers);

        return restTemplate.postForObject(
                senseMailProperties.getBaseUrl() + "/api/mail/send",
                entity,
                SenseMailResponse.class
        );
    }
}