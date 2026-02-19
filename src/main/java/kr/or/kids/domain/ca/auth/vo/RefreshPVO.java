package kr.or.kids.domain.ca.auth.vo;

import java.math.BigInteger;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "JWT Token Refresh 요청", description = "JWT Token Refresh 요청 Parameter VO")
public class RefreshPVO {
    /**
     * JWT토큰ID
     */
    @Schema(requiredMode = RequiredMode.REQUIRED, description = "JWT토큰ID", type = "BigInteger")
    private BigInteger tokenId;

    /**
     * JWT_Refresh_Token
     */
    @Schema(description = "JWT_Refresh_Token", type = "String")
    private String refreshToken;

    public BigInteger getTokenId()
    {
        return tokenId;
    }
    public void setTokenId(BigInteger tokenId)
    {
        this.tokenId = tokenId;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
