package kr.or.kids.domain.ca.auth.service;

public interface TokenBlacklistService {

    void blacklist(String token, long ttlMillis);

    boolean isBlacklisted(String token);

}