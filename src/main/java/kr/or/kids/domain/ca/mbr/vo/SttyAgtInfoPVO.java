package kr.or.kids.domain.ca.mbr.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "대국민포털_법정대리인정보기본", description = "대국민포털_법정대리인정보기본 Search Parameter VO")
public class SttyAgtInfoPVO
{
    /**
     * 회원번호
     */
    @Schema(description = "회원번호", type = "String")
    private String mbrNo;

    /**
     * 법정대리인명
     */
    @Schema(description = "법정대리인명", type = "String")
    private String sttyAgtNm;

    /**
     * 법정대리인암호화전화번호
     */
    @Schema(description = "법정대리인암호화전화번호", type = "String")
    private String sttyAgtEncptTelno;

    /**
     * 법정대리인관계
     */
    @Schema(description = "법정대리인관계", type = "String")
    private String sttyAgtRel;

    /**
     * 연계정보식별아이디
     */
    @Schema(description = "연계정보식별아이디", type = "String")
    private String linkInfoIdntfId;

    /**
     * 인증토큰
     */
    @Schema(description = "인증토큰", type = "String")
    private String certToken;

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

    public String getMbrNo()
    {
        return mbrNo;
    }
    public void setMbrNo(String mbrNo)
    {
        this.mbrNo = mbrNo;
    }
    public String getSttyAgtNm()
    {
        return sttyAgtNm;
    }
    public void setSttyAgtNm(String sttyAgtNm)
    {
        this.sttyAgtNm = sttyAgtNm;
    }
    public String getSttyAgtEncptTelno()
    {
        return sttyAgtEncptTelno;
    }
    public void setSttyAgtEncptTelno(String sttyAgtEncptTelno)
    {
        this.sttyAgtEncptTelno = sttyAgtEncptTelno;
    }
    public String getSttyAgtRel()
    {
        return sttyAgtRel;
    }
    public void setSttyAgtRel(String sttyAgtRel)
    {
        this.sttyAgtRel = sttyAgtRel;
    }
    public String getLinkInfoIdntfId()
    {
        return linkInfoIdntfId;
    }
    public void setLinkInfoIdntfId(String linkInfoIdntfId)
    {
        this.linkInfoIdntfId = linkInfoIdntfId;
    }
    public String getCertToken()
    {
        return certToken;
    }
    public void setCertToken(String certToken)
    {
        this.certToken = certToken;
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