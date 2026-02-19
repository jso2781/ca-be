package kr.or.kids.domain.ca.auth.service.impl;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import kr.or.kids.domain.ca.auth.service.TokenBlacklistService;
import kr.or.kids.global.util.DrugsafeUtil;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final StringRedisTemplate redisTemplate;

    public TokenBlacklistServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String key(String token) {
        // token이 길어서 SHA-256으로 키 축약 추천
        String hashed = DrugsafeUtil.sha256Hex(token);
        return "jwt:blacklist:" + hashed;
    }

    public void blacklist(String token, long ttlMillis) {
        if (token == null || token.isBlank()) return;
        if (ttlMillis <= 0) return;
        redisTemplate.opsForValue().set(key(token), "1", Duration.ofMillis(ttlMillis));
    }

    public boolean isBlacklisted(String token) {
        if (token == null || token.isBlank()) return false;
        Boolean hasKey = redisTemplate.hasKey(key(token));
        return Boolean.TRUE.equals(hasKey);
    }

    public void unblacklist(String token) {
        if (token == null || token.isBlank()) return;
        redisTemplate.delete(key(token));
    }
}
