package kr.or.kids.domain.ca.msg.client.surem;

import kr.or.kids.domain.ca.msg.client.surem.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SuremMessageClient {

    @Value("${spring.surem.baseUrl:https://rest.surem.com}")
    private String baseUrl;

    @Value("${spring.surem.rcsUrl:https://rcs-api.surem.com}")
    private String rcsBaseUrl;

    @Value("${spring.surem.userCode}")
    private String userCode;

    @Value("${spring.surem.secretKey}")
    private String secretKey;

    private WebClient webClient;
    private WebClient rcsWebClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.rcsWebClient = WebClient.builder()
                .baseUrl(rcsBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /* ===== 인증 ===== */
    public String getToken() {
        Map<String, String> body = new HashMap<>();
        body.put("userCode", userCode);
        body.put("secretKey", secretKey);

        Map res = webClient.post()
                .uri("/api/v1/auth/token")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        return (String) res.get("token");
    }

    /* ===== 메시지 발송 ===== */
    public SuremApiSendResponse send(SuremApiSendRequest req, String token, String kind) {
        if ("R".equals(kind)) {
            return rcsWebClient.post()
                    .uri("/api/v1/send/rcs")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(req)
                    .retrieve()
                    .bodyToMono(SuremApiSendResponse.class)
                    .block();
        }
        String uri = switch (kind) {
            case "S" -> "/api/v1/send/sms";
            case "M" -> "/api/v1/send/mms";
            case "T" -> "/api/v1/send/alimtalk";
            case "I" -> "/api/v1/send/intl";
            default  -> throw new IllegalArgumentException("유효하지 않은 KIND: " + kind);
        };
        return webClient.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(SuremApiSendResponse.class)
                .block();
    }

    /* ===== 전송결과 Polling ===== */
    public SuremReportResponse getReport(String type) {
        return webClient.get()
                .uri(u -> u.path("/api/v2/report/responseAll")
                        .queryParam("type", type).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .retrieve()
                .bodyToMono(SuremReportResponse.class)
                .block();
    }

    public SuremBaseResponse completeReport(String checksum) {
        Map<String, String> body = new HashMap<>();
        body.put("checksum", checksum);
        return webClient.post()
                .uri("/api/v2/report/complete")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(SuremBaseResponse.class)
                .block();
    }

    /* ===== 수신거부 ===== */
    public SuremOptOutResponse getOptOutList() {
        return webClient.get()
                .uri("/api/v1/optout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .retrieve()
                .bodyToMono(SuremOptOutResponse.class)
                .block();
    }

    public SuremOptOutMarkResponse markOptOut(List<Integer> idList) {
        Map<String, Object> body = new HashMap<>();
        body.put("idList", idList);
        return webClient.post()
                .uri("/api/v1/optout/mark")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(SuremOptOutMarkResponse.class)
                .block();
    }

    /* ===== 이미지 업로드 ===== */
    public String uploadMmsImage(MultipartFile file) {
        LinkedMultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("image1", file.getResource());
        return webClient.post()
                .uri("/api/v1/image")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .body(BodyInserters.fromMultipartData(form))
                .retrieve()
                .bodyToMono(SuremImageResponse.class)
                .block()
                .getData().getImageKey();
    }

    public String uploadKakaoImage(MultipartFile file, String bizType) {
        LinkedMultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("bizType", bizType);
        form.add("image1", file.getResource());
        return webClient.post()
                .uri("/api/v1/image/kakao")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .body(BodyInserters.fromMultipartData(form))
                .retrieve()
                .bodyToMono(SuremKakaoImageResponse.class)
                .block()
                .getData();
    }

    public String uploadRcsImage(MultipartFile file, String brandId) {
        LinkedMultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("brandId", brandId);
        form.add("image", file.getResource());
        return rcsWebClient.post()
                .uri("/api/v1/image/rcs")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .body(BodyInserters.fromMultipartData(form))
                .retrieve()
                .bodyToMono(SuremImageResponse.class)
                .block()
                .getData().getFileId();
    }

    /* ===== 캠페인 ===== */
    public SuremCampaignResponse createCampaign(SuremCampaignRequest req) {
        return webClient.post()
                .uri("/api/v1/campaign/create")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(req)
                .retrieve()
                .bodyToMono(SuremCampaignResponse.class)
                .block();
    }

    /* ===== 알림톡 템플릿 ===== */
    public SuremBaseResponse createTemplate(Map<String, Object> req) {
        return webClient.post().uri("/api/v1/template/create")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(req).retrieve().bodyToMono(SuremBaseResponse.class).block();
    }

    public SuremTemplateResponse getTemplate(String senderKey, String templateCode) {
        return webClient.get()
                .uri(u -> u.path("/api/v1/template")
                        .queryParam("senderKey", senderKey)
                        .queryParam("templateCode", templateCode).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .retrieve().bodyToMono(SuremTemplateResponse.class).block();
    }

    public SuremTemplateListResponse getTemplateList(String senderKey, int page, int count) {
        return webClient.get()
                .uri(u -> u.path("/api/v1/template/list")
                        .queryParam("senderKey", senderKey)
                        .queryParam("page", page)
                        .queryParam("count", count).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .retrieve().bodyToMono(SuremTemplateListResponse.class).block();
    }

    public SuremBaseResponse updateTemplate(Map<String, Object> req) {
        return webClient.post().uri("/api/v1/template/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(req).retrieve().bodyToMono(SuremBaseResponse.class).block();
    }

    public SuremBaseResponse requestTemplate(String senderKey, String templateCode) {
        return webClient.post().uri("/api/v1/template/request")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(Map.of("senderKey", senderKey, "senderKeyType", "S", "templateCode", templateCode))
                .retrieve().bodyToMono(SuremBaseResponse.class).block();
    }

    public SuremBaseResponse cancelTemplateRequest(String senderKey, String templateCode) {
        return webClient.post().uri("/api/v1/template/request/cancel")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(Map.of("senderKey", senderKey, "senderKeyType", "S", "templateCode", templateCode))
                .retrieve().bodyToMono(SuremBaseResponse.class).block();
    }

    public SuremBaseResponse deleteTemplate(String senderKey, String templateCode) {
        return webClient.post().uri("/api/v1/template/delete")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(Map.of("senderKey", senderKey, "templateCode", templateCode))
                .retrieve().bodyToMono(SuremBaseResponse.class).block();
    }

    public SuremBaseResponse releaseTemplateDormant(String senderKey, String templateCode) {
        return webClient.post().uri("/api/v1/template/dormant/release")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(Map.of("senderKey", senderKey, "senderKeyType", "S", "templateCode", templateCode))
                .retrieve().bodyToMono(SuremBaseResponse.class).block();
    }

    public SuremBaseResponse getTemplateCategory() {
        return webClient.get().uri("/api/v1/template/category")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .retrieve().bodyToMono(SuremBaseResponse.class).block();
    }

    /* ===== 카카오 발신프로필 ===== */
    public SuremBaseResponse requestProfileToken(String yellowId, String phoneNumber) {
        return webClient.post().uri("/api/v1/profile/token")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(Map.of("yellowId", yellowId, "phoneNumber", phoneNumber))
                .retrieve().bodyToMono(SuremBaseResponse.class).block();
    }

    public SuremProfileCreateResponse createProfile(Map<String, Object> req) {
        return webClient.post().uri("/api/v1/profile/create")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(req).retrieve().bodyToMono(SuremProfileCreateResponse.class).block();
    }

    public SuremBaseResponse getProfileList() {
        return webClient.get().uri("/api/v1/profile/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .retrieve().bodyToMono(SuremBaseResponse.class).block();
    }

    public SuremBaseResponse getProfile(String senderKey) {
        return webClient.get()
                .uri(u -> u.path("/api/v1/profile").queryParam("senderKey", senderKey).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .retrieve().bodyToMono(SuremBaseResponse.class).block();
    }

    public SuremBaseResponse releaseProfileDormant(String senderKey) {
        return webClient.post().uri("/api/v1/profile/dormant/release")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(Map.of("senderKey", senderKey))
                .retrieve().bodyToMono(SuremBaseResponse.class).block();
    }

    public SuremBaseResponse updateUnsubscribe(String senderKey, String unsubscribePhone, String unsubscribeAuth) {
        Map<String, String> body = new HashMap<>();
        body.put("senderKey", senderKey);
        body.put("unsubscribePhone", unsubscribePhone);
        if (unsubscribeAuth != null) body.put("unsubscribeAuth", unsubscribeAuth);
        return webClient.post().uri("/api/v1/profile/unsubscribe/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .bodyValue(body).retrieve().bodyToMono(SuremBaseResponse.class).block();
    }
}
