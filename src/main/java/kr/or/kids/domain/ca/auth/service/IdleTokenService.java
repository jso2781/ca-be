package kr.or.kids.domain.ca.auth.service;

public interface IdleTokenService {

    /** 로그아웃 시 제거(선택) */
    void delete(String tokenSn);

    /** 요청 들어왔을 때 idle 만료 여부 확인 */
    boolean exists(String tokenSn);

    /** 로그인/refresh 시 or 요청 시마다 TTL을 30분으로 리셋 */
    void touch(String tokenSn);

}