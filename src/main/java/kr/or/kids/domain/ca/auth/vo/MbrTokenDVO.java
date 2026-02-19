package kr.or.kids.domain.ca.auth.vo;

import java.math.BigInteger;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;


@Schema(name = "대국민포털_회원_TOKEN", description = "대국민포털_회원_TOKEN Delete Parameter VO")
public class MbrTokenDVO
{
    /**
     * JWT토큰일련번호
     */
    @Schema(requiredMode = RequiredMode.REQUIRED, description = "JWT토큰일련번호", type = "BigInteger")
    private BigInteger tokenSn;

    /**
     * 회원아이디
     */
    @Schema(description = "회원아이디", type = "String")
    private String mbrId;

    public BigInteger getTokenSn()
    {
        return tokenSn;
    }
    public void setTokenSn(BigInteger tokenSn)
    {
        this.tokenSn = tokenSn;
    }
    public String getMbrId() {
        return mbrId;
    }
    public void setMbrId(String mbrId) {
        this.mbrId = mbrId;
    }
}