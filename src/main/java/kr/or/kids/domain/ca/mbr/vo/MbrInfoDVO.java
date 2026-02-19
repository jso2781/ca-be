package kr.or.kids.domain.ca.mbr.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "대국민포털_회원정보기본", description = "대국민포털_회원정보기본 Delete Parameter VO")
public class MbrInfoDVO
{
    /**
     * 회원번호
     */
    @Schema(requiredMode = RequiredMode.REQUIRED, description = "회원번호", type = "String")
    private String mbrNo;

    public String getMbrNo()
    {
        return mbrNo;
    }
    public void setMbrNo(String mbrNo)
    {
        this.mbrNo = mbrNo;
    }

}