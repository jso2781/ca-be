package kr.or.kids.domain.ca.adr.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.kids.domain.ca.adr.service.AdressCntcService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdressCntcServiceImpl implements AdressCntcService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${address.openapi.key}")
    private String confmKey;

    @Value("${address.openapi.url}")
    private String openApiUrl;

    /**
     * 도로명주소 Open API 기반 주소 목록 조회
     */
   /* @Override
    public ApiPrnDto search(String keyword, int pageNum, int pageSize) {

        try {
            // ✅ 반드시 직접 인코딩
            String encodedKey =
                    URLEncoder.encode(confirmKey, StandardCharsets.UTF_8);
            String encodedKeyword =
                    URLEncoder.encode(keyword, StandardCharsets.UTF_8);

            String url = UriComponentsBuilder
                    .fromHttpUrl(openApiUrl) // 반드시 addrLinkApi.do
                    .queryParam("confmKey", encodedKey)
                    .queryParam("keyword", encodedKeyword)
                    .queryParam("currentPage", pageNum)
                    .queryParam("countPerPage", pageSize)
                    .queryParam("resultType", "json")
                    .build(false) // ⭐ 중요
                    .toUriString();

            log.info(">>> JUSO CALL URL = {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.setAccept(MediaType.parseMediaTypes("application/json"));

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            String.class
                    );

            log.info(">>> JUSO RESPONSE = {}", response.getBody());

            Map<String, Object> body =
                    objectMapper.readValue(
                            response.getBody(),
                            new TypeReference<Map<String, Object>>() {}
                    );

            return ApiPrnDto.success(new HashMap<>(body));

        } catch (Exception e) {
            log.error("주소 OpenAPI 호출 실패", e);
            return ApiPrnDto.fail(ApiResultCode.SYSTEM_ERROR);
        }
    }*/
    @Override
    public String search(String keyword, int currentPage, int countPerPage) {

        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("keyword is required");
        }

        String url = UriComponentsBuilder
                .fromHttpUrl("http://business.juso.go.kr/addrink/addrLinkUrl.do")
                .queryParam("confmKey", confmKey)     // 🔥 인코딩 금지
                .queryParam("currentPage", currentPage)
                .queryParam("countPerPage", countPerPage)
                .queryParam("keyword", keyword)       // 🔥 한글 그대로
                .queryParam("resultType", "xml")
                .build(false)
                .toUriString();

        log.info("JUSO API CALL = {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, "Mozilla/5.0");
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }


    // 로그로 찍어보기 (임시)
//    @PostConstruct
//    public void checkKey() {
//        System.out.println("JUSO KEY = " + confmKey);
//        System.out.println("JUSO URL = " + openApiUrl);
//
//    }
}

