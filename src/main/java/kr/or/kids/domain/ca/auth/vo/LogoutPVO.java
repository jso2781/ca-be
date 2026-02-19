package kr.or.kids.domain.ca.auth.vo;

import java.math.BigInteger;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "로그아웃 요청", description = "로그아웃 요청 Parameter VO")
public class LogoutPVO {
    /**
     * JWT토큰일련번호
     */
    @Schema(requiredMode = RequiredMode.REQUIRED, description = "JWT토큰일련번호", type = "BigInteger")
    private BigInteger tokenSn;

    public BigInteger getTokenSn() {
        return tokenSn;
    }

    public void setTokenSn(BigInteger tokenSn) {
        this.tokenSn = tokenSn;
    }
}
