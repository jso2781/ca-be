package kr.or.kids.domain.ca.adr.service.impl;

import kr.or.kids.domain.ca.adr.service.AdressCntcService;
import kr.or.kids.domain.ca.adr.vo.JusoResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AdressCntcServiceImpl implements AdressCntcService {

    @Value("${spring.address.openapi.key}")
    private String apiKey;

    @Value("${spring.address.openapi.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 주소 검색 API 호출 (XML 방식)
     * @param keyword      검색 키워드
     * @param currentPage  현재 페이지
     * @param countPerPage 페이지당 건수
     * @return JusoResponseVO
     */
    @Override
    public JusoResponseVO search(String keyword, int currentPage, int countPerPage) throws Exception {

        log.info("주소 검색 API 호출 - keyword: {}, page: {}, count: {}", keyword, currentPage, countPerPage);

        // 1. URI 빌드 (한글 인코딩 처리)
        URI uri = UriComponentsBuilder
                .fromHttpUrl(apiUrl)
                .queryParam("confmKey", apiKey)
                .queryParam("currentPage", currentPage)
                .queryParam("countPerPage", countPerPage)
                .queryParam("keyword", keyword)
                .queryParam("resultType", "xml")  // ✅ XML 방식
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        log.debug("주소 API URL: {}", uri);

        // 2. HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // 3. 외부 API 호출 - String으로 받기
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            String responseBody = responseEntity.getBody();
            log.debug("주소 API 응답 Content-Type: {}", responseEntity.getHeaders().getContentType());
            log.debug("주소 API 응답 Body (first 200 chars): {}",
                    responseBody != null && responseBody.length() > 200
                            ? responseBody.substring(0, 200)
                            : responseBody);

            // 4. XML → JusoResponseVO 파싱
            JusoResponseVO response = parseXmlToJusoResponse(responseBody);

            log.info("주소 검색 결과 - 총 {}건",
                    response != null && response.getResults() != null && response.getResults().getCommon() != null
                            ? response.getResults().getCommon().getTotalCount()
                            : 0);

            return response;

        } catch (Exception e) {
            log.error("주소 API 호출 실패 - {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * XML 문자열을 JusoResponseVO로 파싱
     */
    private JusoResponseVO parseXmlToJusoResponse(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        doc.getDocumentElement().normalize();

        JusoResponseVO response = new JusoResponseVO();
        JusoResponseVO.Results results = new JusoResponseVO.Results();

        // Common 파싱
        JusoResponseVO.Common common = new JusoResponseVO.Common();
        Element commonElement = (Element) doc.getElementsByTagName("common").item(0);
        if (commonElement != null) {
            common.setErrorMessage(getTagValue("errorMessage", commonElement));
            common.setCountPerPage(getTagValue("countPerPage", commonElement));
            common.setTotalCount(getTagValue("totalCount", commonElement));
            common.setErrorCode(getTagValue("errorCode", commonElement));
            common.setCurrentPage(getTagValue("currentPage", commonElement));
        }
        results.setCommon(common);

        // Juso 리스트 파싱
        List<JusoResponseVO.Juso> jusoList = new ArrayList<>();
        NodeList jusoNodes = doc.getElementsByTagName("juso");

        for (int i = 0; i < jusoNodes.getLength(); i++) {
            Element jusoElement = (Element) jusoNodes.item(i);
            JusoResponseVO.Juso juso = new JusoResponseVO.Juso();

            juso.setRoadAddr(getTagValue("roadAddr", jusoElement));
            juso.setRoadAddrPart1(getTagValue("roadAddrPart1", jusoElement));
            juso.setRoadAddrPart2(getTagValue("roadAddrPart2", jusoElement));
            juso.setJibunAddr(getTagValue("jibunAddr", jusoElement));
            juso.setEngAddr(getTagValue("engAddr", jusoElement));
            juso.setZipNo(getTagValue("zipNo", jusoElement));
            juso.setAdmCd(getTagValue("admCd", jusoElement));
            juso.setRnMgtSn(getTagValue("rnMgtSn", jusoElement));
            juso.setBdMgtSn(getTagValue("bdMgtSn", jusoElement));
            juso.setDetBdNmList(getTagValue("detBdNmList", jusoElement));
            juso.setBdNm(getTagValue("bdNm", jusoElement));
            juso.setBdKdcd(getTagValue("bdKdcd", jusoElement));
            juso.setSiNm(getTagValue("siNm", jusoElement));
            juso.setSggNm(getTagValue("sggNm", jusoElement));
            juso.setEmdNm(getTagValue("emdNm", jusoElement));
            juso.setLiNm(getTagValue("liNm", jusoElement));
            juso.setRn(getTagValue("rn", jusoElement));
            juso.setUdrtYn(getTagValue("udrtYn", jusoElement));
            juso.setBuldMnnm(getTagValue("buldMnnm", jusoElement));
            juso.setBuldSlno(getTagValue("buldSlno", jusoElement));
            juso.setMtYn(getTagValue("mtYn", jusoElement));
            juso.setLnbrMnnm(getTagValue("lnbrMnnm", jusoElement));
            juso.setLnbrSlno(getTagValue("lnbrSlno", jusoElement));
            juso.setEmdNo(getTagValue("emdNo", jusoElement));

            jusoList.add(juso);
        }
        results.setJuso(jusoList);

        response.setResults(results);
        return response;
    }

    /**
     * XML Element에서 태그 값 추출
     */
    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            Element tagElement = (Element) nodeList.item(0);
            if (tagElement != null && tagElement.getFirstChild() != null) {
                return tagElement.getFirstChild().getNodeValue();
            }
        }
        return null;
    }
}