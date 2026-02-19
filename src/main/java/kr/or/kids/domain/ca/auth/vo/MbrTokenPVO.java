package kr.or.kids.domain.ca.auth.vo;

import java.math.BigInteger;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "대국민포털_회원_TOKEN", description = "대국민포털_회원_TOKEN Search Parameter VO")
public class MbrTokenPVO
{
    /**
     * JWT토큰ID
     */
    @Schema(description = "JWT토큰ID", type = "BigInteger")
    private BigInteger tokenSn;

    /**
     * 회원아이디
     */
    @Schema(description = "회원아이디", type = "String")
    private String mbrId;

    /**
     * 회원암호화비밀번호
     */
    @Schema(description = "회원암호화비밀번호", type = "String")
    private String encptMbrPswd;

    /**
     * 애플리케이션ID
     */
    @Schema(description = "애플리케이션ID", type = "String")
    private String prgrmId;

    /**
     * JWT_Refresh_Token
     */
    @Schema(description = "JWT_Refresh_Token", type = "String")
    private String updtTokenCn;

    /**
     * JWT_Access_Token
     */
    @Schema(description = "JWT_Access_Token", type = "String")
    private String acsTokenCn;

    /**
     * 등록자아이디
     */
    @Schema(description = "등록자아이디", type = "String")
    private String rgtrId;

    /**
     * 등록일시
     */
    @Schema(description = "등록일시", type = "String")
    private String regDt;

    /**
     * 등록프로그램아이디
     */
    @Schema(description = "등록프로그램아이디", type = "String")
    private String regPrgrmId;

    /**
     * 수정자아이디
     */
    @Schema(description = "수정자아이디", type = "String")
    private String mdfrId;

    /**
     * 수정일시
     */
    @Schema(description = "수정일시", type = "String")
    private String mdfcnDt;

    /**
     * 수정프로그램아이디
     */
    @Schema(description = "수정프로그램아이디", type = "String")
    private String mdfcnPrgrmId;

    public BigInteger getTokenSn()
    {
        return tokenSn;
    }
    public void setTokenSn(BigInteger tokenSn)
    {
        this.tokenSn = tokenSn;
    }
    public String getEncptMbrPswd() {
        return encptMbrPswd;
    }
    public void setEncptMbrPswd(String encptMbrPswd) {
        this.encptMbrPswd = encptMbrPswd;
    }
    public String getMbrId()
    {
        return mbrId;
    }
    public void setMbrId(String mbrId)
    {
        this.mbrId = mbrId;
    }
    public String getPrgrmId()
    {
        return prgrmId;
    }
    public void setPrgrmId(String prgrmId)
    {
        this.prgrmId = prgrmId;
    }
    public String getUpdtTokenCn()
    {
        return updtTokenCn;
    }
    public void setUpdtTokenCn(String updtTokenCn)
    {
        this.updtTokenCn = updtTokenCn;
    }
    public String getAcsTokenCn()
    {
        return acsTokenCn;
    }
    public void setAcsTokenCn(String acsTokenCn)
    {
        this.acsTokenCn = acsTokenCn;
    }
    public String getRgtrId()
    {
        return rgtrId;
    }
    public void setRgtrId(String rgtrId)
    {
        this.rgtrId = rgtrId;
    }
    public String getRegDt()
    {
        return regDt;
    }
    public void setRegDt(String regDt)
    {
        this.regDt = regDt;
    }
    public String getRegPrgrmId()
    {
        return regPrgrmId;
    }
    public void setRegPrgrmId(String regPrgrmId)
    {
        this.regPrgrmId = regPrgrmId;
    }
    public String getMdfrId()
    {
        return mdfrId;
    }
    public void setMdfrId(String mdfrId)
    {
        this.mdfrId = mdfrId;
    }
    public String getMdfcnDt()
    {
        return mdfcnDt;
    }
    public void setMdfcnDt(String mdfcnDt)
    {
        this.mdfcnDt = mdfcnDt;
    }
    public String getMdfcnPrgrmId()
    {
        return mdfcnPrgrmId;
    }
    public void setMdfcnPrgrmId(String mdfcnPrgrmId)
    {
        this.mdfcnPrgrmId = mdfcnPrgrmId;
    }

}