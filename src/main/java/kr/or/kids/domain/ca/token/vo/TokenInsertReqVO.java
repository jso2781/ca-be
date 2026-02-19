package kr.or.kids.domain.ca.token.vo;

import lombok.Data;

/**
 * 토큰 등록 요청 VO
 */
@Data
public class TokenInsertReqVO {
    private Integer adminId;        // 관리자 번호
    private Integer userId;         // 사용자번호
    private String prgrmId;           // 앱ID
    private String updtTokenCn;    // 갱신토큰내용
    private String acsTokenCn;     // 접근토큰내용
    private Integer inAdminId;      // 등록 관리자 번호
    private Integer upAdminId;      // 수정 관리자 번호
}