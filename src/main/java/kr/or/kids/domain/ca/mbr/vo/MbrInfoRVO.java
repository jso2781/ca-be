package kr.or.kids.domain.ca.mbr.vo;

import java.math.BigInteger;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "대국민포털_회원정보기본", description = "대국민포털_회원정보기본 Search Result VO")
public class MbrInfoRVO
{
    /**
     * 회원번호
     */
    @Schema(description = "회원번호", type = "String")
    private String mbrNo;

    /**
     * 회원아이디
     */
    @Schema(description = "회원아이디", type = "String")
    private String mbrId;

    /**
     * 회원암호화성명
     */
    @Schema(description = "회원암호화성명", type = "String")
    private String encptMbrFlnm;

    /**
     * 회원암호화이메일
     */
    @Schema(description = "회원암호화이메일", type = "String")
    private String encptMbrEmlNm;

    /**
     * 회원암호화비밀번호
     */
    @Schema(description = "회원암호화비밀번호", type = "String")
    private String encptMbrPswd;

    /**
     * 회원암호화비밀번호
     */
    @Schema(description = "회원암호화비밀번호", type = "String")
    private String mbrEnpswd;


    /**
     * 회원암호화비밀번호
     */
    @Schema(description = "회원암호화비밀번호", type = "String")
    private String toKenId;

    /**
     * 회원암호화전화번호
     */
    @Schema(description = "회원암호화전화번호", type = "String")
    private String encptMbrTelno;

    /**
     * 회원유형코드
     */
    @Schema(description = "회원유형코드", type = "String")
    private String mbrTypeCd;

    /**
     * 회원가입상태
     */
    @Schema(description = "회원가입상태", type = "String")
    private String mbrJoinSttsCd;

    /**
     * 회원가입일시
     */
    @Schema(description = "회원가입일시", type = "String")
    private String mbrJoinDt;

    /**
     * 회원탈퇴사유
     */
    @Schema(description = "회원탈퇴사유", type = "String")
    private String mbrWhdwlRsn;

    /**
     * 회원탈퇴일시
     */
    @Schema(description = "회원탈퇴일시", type = "String")
    private String mbrWhdwlDt;

    /**
     * 자문위원여부
     */
    @Schema(description = "자문위원여부", type = "String")
    private String cnstnMbcmtYn;

    /**
     * 이전암호화비밀번호
     */
    @Schema(description = "이전암호화비밀번호", type = "String")
    private String encptBfrPswd;

    /**
     * 비밀번호변경일시
     */
    @Schema(description = "비밀번호변경일시", type = "String")
    private String pswdChgDt;

    /**
     * 비밀번호오류횟수
     */
    @Schema(description = "비밀번호오류횟수", type = "Integer")
    private Integer pswdErrNmtm = 0;

    /**
     * 연계정보식별아이디
     */
    @Schema(description = "연계정보식별아이디", type = "String")
    private String linkInfoIdntfId;

    /**
     * 인증토큰
     */
    @Schema(description = "인증토큰", type = "String")
    private String certTokenVl;

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
     * JWT토큰일련번호
     */
    @Schema(description = "JWT토큰일련번호", type = "BigInteger")
    private BigInteger tokenSn;

    /**
     * 프로그램아이디
     */
    @Schema(description = "프로그램아이디", type = "String")
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
     * 전문가 회원 여부(Y/N)
     */
    @Schema(description = "전문가 회원 여부(Y/N)", type = "String")
    private String expertYn;

    private List<InstTaskItem> apprInstTaskList;

    @Getter
    @Setter
    @Schema(name = "전문가 승인권자의 기관 업무 조회 결과", description = "전문가 승인권자의 기관 업무 조회 결과 VO")
    public static class InstTaskItem {
        /**
         * 사업자등록번호
         */
        @Schema(description = "사업자등록번호", type = "String")
        private String brno;

        /**
         * 사업자업무관리번호
         */
        @Schema(description = "사업자업무관리번호", type = "String")
        private String bzmnTaskMngNo;
    }
}