package kr.or.kids.domain.ca.msg.client.surem;

import kr.or.kids.domain.ca.msg.client.surem.dto.SuremApiSendRequest;
import kr.or.kids.domain.ca.msg.client.surem.dto.SuremApiSendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component   // 🔥 이거 없으면 Bean 안 됨
@RequiredArgsConstructor
public class SuremMessageClient {

    private final WebClient suremWebClient;

    public SuremApiSendResponse send(SuremApiSendRequest req) {
        return suremWebClient.post()
                .uri("/api/v1/msg/send")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(SuremApiSendResponse.class)
                .block();
    }
}
