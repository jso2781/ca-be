package kr.or.kids.domain.ca.auth.service;

public interface ActiveTokenService {

    void markActive(String mbrId, String tokenSn, long ttlMillis);

    void revoke(String mbrId, String tokenSn);

    boolean isActive(String mbrId, String tokenSn);

}