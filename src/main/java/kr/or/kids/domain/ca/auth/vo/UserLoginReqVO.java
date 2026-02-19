package kr.or.kids.domain.ca.auth.vo;

import lombok.Data;

@Data
public class UserLoginReqVO {

    private String userLoginId;     // 사용자로그인ID
    private String userPw;          // 사용자명

}
