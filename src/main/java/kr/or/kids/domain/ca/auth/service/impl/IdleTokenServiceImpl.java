package kr.or.kids.domain.ca.auth.service.impl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import kr.or.kids.domain.ca.auth.service.IdleTokenService;

@Service
public class IdleTokenServiceImpl implements IdleTokenService {
    private final StringRedisTemplate redisTemplate;

    // 프론트 idle 30분과 동일하게
    private final long idleTtlMillis;

    public IdleTokenServiceImpl(StringRedisTemplate redisTemplate, @Value("${jwt.idle-timeout-millis:1800000}") long idleTtlMillis){
        this.redisTemplate = redisTemplate;
        this.idleTtlMillis = idleTtlMillis;
    }

    private String key(String tokenSn) {
        return "jwt:idle:" + tokenSn;
    }

    /** 로그인/refresh 시 or 요청 시마다 TTL을 30분으로 리셋 */
    @Override
    public void touch(String tokenSn) {
        if (tokenSn == null || tokenSn.isBlank()) return;
        redisTemplate.opsForValue().set(key(tokenSn), "1", Duration.ofMillis(idleTtlMillis));
    }

    /** 요청 들어왔을 때 idle 만료 여부 확인 */
    @Override
    public boolean exists(String tokenSn) {
        if (tokenSn == null || tokenSn.isBlank()) return false;
        Boolean has = redisTemplate.hasKey(key(tokenSn));
        return Boolean.TRUE.equals(has);
    }

    /** 로그아웃 시 제거(선택) */
    @Override
    public void delete(String tokenSn) {
        if (tokenSn == null || tokenSn.isBlank()) return;
        redisTemplate.delete(key(tokenSn));
    }
}
