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

    private String key(String mbrId, String tokenSn) {
        return "jwt:active:" + mbrId + ":" + tokenSn;
    }

    @Override
    public void markActive(String mbrId, String tokenSn, long ttlMillis) {
        if(ttlMillis <= 0)return;
        redisTemplate.opsForValue().set(key(mbrId, tokenSn), "1", Duration.ofMillis(ttlMillis));
    }

    @Override
    public void revoke(String mbrId, String tokenSn) {
        redisTemplate.delete(key(mbrId, tokenSn));
    }

    @Override
    public boolean isActive(String mbrId, String tokenSn) {
        Boolean exists = redisTemplate.hasKey(key(mbrId, tokenSn));
        return Boolean.TRUE.equals(exists);
    }
}
