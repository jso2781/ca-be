package kr.or.kids.domain.ca.anyid.dto;

import java.util.Map;

/**
 * SDK decrypt 결과에서 ssobStr(복호화된 JSON)을 파싱한 값 요약.
 */
public record AnyIdLoginResponse(
        String status,
        String ci,
        String name,
        Integer authLvl,
        String group,
        String timestamp,
        String clientIp,
        Map<String, Object> raw
) {}
