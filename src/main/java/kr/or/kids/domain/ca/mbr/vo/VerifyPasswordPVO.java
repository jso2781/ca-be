package kr.or.kids.domain.ca.mbr.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "회원번호(mbr_id), 패스워드(encpt_mbr_pswd)를 기준으로 조회시 데이터가 존재하는지 체크", description = "회원번호(mbr_id), 패스워드(encpt_mbr_pswd)를 기준으로 조회시 데이터가 존재하는지 체크용 Parameter VO")
@Data
public class VerifyPasswordPVO
{
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
}