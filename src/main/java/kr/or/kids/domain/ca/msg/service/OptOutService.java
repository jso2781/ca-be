// OptOutService.java
package kr.or.kids.domain.ca.msg.service;

import kr.or.kids.domain.ca.msg.client.surem.SuremMessageClient;
import kr.or.kids.domain.ca.msg.client.surem.dto.SuremOptOutMarkResponse;
import kr.or.kids.domain.ca.msg.client.surem.dto.SuremOptOutResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptOutService {

    private final SuremMessageClient client;

    /** 조회 + 마킹 한번에 */
    public SuremOptOutResponse getAndMarkOptOutList() {
        SuremOptOutResponse res = client.getOptOutList();

        if (!"1".equals(res.getCode()) || res.getData() == null || res.getData().isEmpty()) {
            log.info("[OptOut] 수신거부 데이터 없음 code={}", res.getCode());
            return res;
        }

        log.info("[OptOut] 수신거부 조회 건수={}", res.getData().size());

        // TODO: DB 저장 (필요 시 mapper 추가)

        List<Integer> idList = res.getData().stream()
                .map(SuremOptOutResponse.OptOutData::getId)
                .collect(Collectors.toList());

        SuremOptOutMarkResponse markRes = client.markOptOut(idList);

        if (markRes.getData() != null && !markRes.getData().isEmpty()) {
            log.warn("[OptOut] 마킹 실패 ID={}", markRes.getData());
        } else {
            log.info("[OptOut] 마킹 완료 건수={}", idList.size());
        }

        return res;
    }

    public SuremOptOutResponse getOptOutList() {
        return client.getOptOutList();
    }

    public SuremOptOutMarkResponse markOptOut(List<Integer> idList) {
        return client.markOptOut(idList);
    }
}