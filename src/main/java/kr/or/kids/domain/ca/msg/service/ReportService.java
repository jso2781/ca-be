// ReportService.java
package kr.or.kids.domain.ca.msg.service;

import kr.or.kids.domain.ca.msg.client.surem.SuremMessageClient;
import kr.or.kids.domain.ca.msg.client.surem.dto.SuremBaseResponse;
import kr.or.kids.domain.ca.msg.client.surem.dto.SuremReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final SuremMessageClient client;

    /** 결과 조회 후 완료처리까지 한번에 */
    public SuremReportResponse getReportAndComplete(String type) {
        SuremReportResponse res = client.getReport(type);

        if (!"1".equals(res.getCode()) || res.getData() == null || res.getData().isEmpty()) {
            log.info("[REPORT] 결과 없음 type={} code={}", type, res.getCode());
            return res;
        }

        log.info("[REPORT] 결과 조회 type={} 건수={}", type, res.getData().size());

        // TODO: 결과를 DB에 저장 (필요 시 mapper 추가)
        // res.getData().forEach(data -> reportMapper.upsert(data));

        // 완료 처리
        SuremBaseResponse completeRes = client.completeReport(res.getChecksum());
        log.info("[REPORT] 완료처리 code={}", completeRes.getCode());

        return res;
    }

    /** 결과 조회만 */
    public SuremReportResponse getReport(String type) {
        return client.getReport(type);
    }

    /** 완료처리만 */
    public SuremBaseResponse completeReport(String checksum) {
        return client.completeReport(checksum);
    }
}