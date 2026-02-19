package kr.or.kids.domain.ca.adr.vo;

import lombok.Data;

@Data
public class JusoResultVO {
    private String roadAddr;   // 도로명주소
    private String roadAddrPart1;    // 도로명주소 본문
    private String roadAddrPart2;    // 상세주소(건물/동)
    private String jibunAddr;  // 지번주소
    private String zipNo;      // 우편번호
    private String siNm;       // 시도
    private String sggNm;      // 시군구
    private String emdNm;      // 읍면동
    private String bdNm;       // 건물명
    private String detBdNm;    // 상세건물명
}
