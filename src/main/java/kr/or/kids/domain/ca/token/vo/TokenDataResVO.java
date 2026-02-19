package kr.or.kids.domain.ca.token.vo;

import lombok.Data;

/**
 * 토큰 데이터조회 결과 VO
 */
@Data
public class TokenDataResVO {
    private String tokenSn;         // 토큰 ID
    private Integer adminId;        // 관리자 번호
    private Integer userId;         // 사용자번호
    private String prgrmId;           // 앱ID
    private String updtTokenCn;    // 갱신토큰내용
    private String acsTokenCn;     // 접근토큰내용
    private Integer inAdminId;      // 등록 관리자 번호
    private String inDatetime;      // 등록 시간
    private Integer upAdminId;      // 수정 관리자 번호
    private String upDatetime;      // 수정 시간
}