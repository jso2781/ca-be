package kr.or.kids.domain.ca.adr.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 주소 검색 API JSON 응답 VO
 * 도로명주소 API 공식 JSON 응답 구조
 */
@Data
public class JusoResponseVO {

    @JsonProperty("results")
    private Results results;

    @Data
    public static class Results {
        @JsonProperty("common")
        private Common common;

        @JsonProperty("juso")
        private List<Juso> juso;
    }

    /**
     * 공통 응답 정보 (페이징, 에러 코드)
     */
    @Data
    public static class Common {
        @JsonProperty("errorMessage")
        private String errorMessage;          // 에러 메시지

        @JsonProperty("countPerPage")
        private String countPerPage;          // 페이지당 출력 개수

        @JsonProperty("totalCount")
        private String totalCount;            // 총 검색 데이터 수

        @JsonProperty("errorCode")
        private String errorCode;             // 에러 코드 (정상: 0)

        @JsonProperty("currentPage")
        private String currentPage;           // 현재 페이지 번호
    }

    /**
     * 주소 정보
     */
    @Data
    public static class Juso {
        @JsonProperty("detBdNmList")
        private String detBdNmList;           // 상세건물명

        @JsonProperty("engAddr")
        private String engAddr;               // 도로명주소(영문)

        @JsonProperty("rn")
        private String rn;                    // 도로명

        @JsonProperty("emdNm")
        private String emdNm;                 // 읍면동명

        @JsonProperty("zipNo")
        private String zipNo;                 // 우편번호

        @JsonProperty("roadAddrPart2")
        private String roadAddrPart2;         // 도로명주소(참고항목)

        @JsonProperty("emdNo")
        private String emdNo;                 // 읍면동일련번호

        @JsonProperty("sggNm")
        private String sggNm;                 // 시군구명

        @JsonProperty("jibunAddr")
        private String jibunAddr;             // 지번주소

        @JsonProperty("siNm")
        private String siNm;                  // 시도명

        @JsonProperty("roadAddrPart1")
        private String roadAddrPart1;         // 도로명주소(참고항목 제외)

        @JsonProperty("bdNm")
        private String bdNm;                  // 건물명

        @JsonProperty("admCd")
        private String admCd;                 // 행정구역코드

        @JsonProperty("udrtYn")
        private String udrtYn;                // 지하여부(0:지상, 1:지하)

        @JsonProperty("lnbrMnnm")
        private String lnbrMnnm;              // 지번본번(번지)

        @JsonProperty("roadAddr")
        private String roadAddr;              // 전체 도로명주소

        @JsonProperty("lnbrSlno")
        private String lnbrSlno;              // 지번부번(호)

        @JsonProperty("buldMnnm")
        private String buldMnnm;              // 건물본번

        @JsonProperty("bdKdcd")
        private String bdKdcd;                // 공동주택여부(1:공동주택, 0:비공동주택)

        @JsonProperty("liNm")
        private String liNm;                  // 법정리명

        @JsonProperty("rnMgtSn")
        private String rnMgtSn;               // 도로명코드

        @JsonProperty("mtYn")
        private String mtYn;                  // 산여부(0:대지, 1:산)

        @JsonProperty("bdMgtSn")
        private String bdMgtSn;               // 건물관리번호

        @JsonProperty("buldSlno")
        private String buldSlno;              // 건물부번

        // 추가 필드들 (필요 시 매핑)
        @JsonProperty("hemdNm")
        private String hemdNm;                // 관할주민센터

        @JsonProperty("hstryYn")
        private String hstryYn;               // 변동이력여부(0:현행, 1:변동이력)

        @JsonProperty("relJibun")
        private String relJibun;              // 관련지번

        @JsonProperty("sggCd")
        private String sggCd;                 // 시군구코드

        @JsonProperty("siCd")
        private String siCd;                  // 시도코드
    }
}