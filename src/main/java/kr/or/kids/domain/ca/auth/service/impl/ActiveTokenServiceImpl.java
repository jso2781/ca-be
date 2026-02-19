package kr.or.kids.domain.ca.auth.service.impl;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import kr.or.kids.domain.ca.auth.service.ActiveTokenService;

@Service
public class ActiveTokenServiceImpl implements ActiveTokenService {
    private final StringRedisTemplate redisTemplate;

    public ActiveTokenServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String key(String mbrId, String tokenId) {
        return "jwt:active:" + mbrId + ":" + tokenId;
    }

    @Override
    public void markActive(String mbrId, String tokenId, long ttlMillis) {
        if(ttlMillis <= 0)return;
        redisTemplate.opsForValue().set(key(mbrId, tokenId), "1", Duration.ofMillis(ttlMillis));
    }

    @Override
    public void revoke(String mbrId, String tokenId) {
        redisTemplate.delete(key(mbrId, tokenId));
    }

    @Override
    public boolean isActive(String mbrId, String tokenId) {
        Boolean exists = redisTemplate.hasKey(key(mbrId, tokenId));
        return Boolean.TRUE.equals(exists);
    }
}
