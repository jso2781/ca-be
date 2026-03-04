package kr.or.kids.domain.ca.msg.service;

import kr.or.kids.domain.ca.msg.client.surem.SuremMessageClient;
import kr.or.kids.domain.ca.msg.client.surem.dto.SuremApiSendRequest;
import kr.or.kids.domain.ca.msg.client.surem.dto.SuremApiSendResponse;
import kr.or.kids.domain.ca.msg.mapper.MsgImgMapper;
import kr.or.kids.domain.ca.msg.mapper.MsgRsltMapper;
import kr.or.kids.domain.ca.msg.mapper.MsgSndngMapper;
import kr.or.kids.domain.ca.msg.vo.MsgImgVO;
import kr.or.kids.domain.ca.msg.vo.MsgListVO;
import kr.or.kids.domain.ca.msg.vo.MsgRsltVO;
import kr.or.kids.domain.ca.msg.vo.MsgSndngVO;
import kr.or.kids.global.system.common.vo.ApiPrnDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MsgSndngMapper sndngMapper;
    private final MsgRsltMapper rsltMapper;
    private final MsgImgMapper msgImgMapper;
    private final SuremMessageClient client;

    @Value("${spring.surem.testMode:false}")
    private boolean testMode;

    private String normalizeKind(String kind) {
        if (kind == null) return null;
        return switch (kind) {
            case "SMS"           -> "S";
            case "LMS", "MMS"   -> "M";
            case "KAKAO", "TALK" -> "T";
            case "INTL"          -> "I";
            case "RCS"           -> "R";
            case "S","M","T","I","R" -> kind;
            default -> throw new IllegalArgumentException("유효하지 않은 KIND: " + kind);
        };
    }

    private void saveMsgImagesIfAny(MsgSndngVO vo) {
        if (vo.getEtc1() == null && vo.getEtc2() == null && vo.getEtc3() == null) return;

        MsgImgVO img = new MsgImgVO();
        img.setMsgImgSn(msgImgMapper.selectNextMsgImgSn());
        img.setSeqno(vo.getSeqno());

        int cnt = 0;
        if (vo.getEtc1() != null) cnt++;
        if (vo.getEtc2() != null) cnt++;
        if (vo.getEtc3() != null) cnt++;

        img.setFileCnt(cnt);
        img.setFilePath1(vo.getEtc1());
        img.setFilePath2(vo.getEtc2());
        img.setFilePath3(vo.getEtc3());
        img.setBiztype(vo.getBiztype());
        img.setUsercode(vo.getUsercode());
        img.setRgtrId(vo.getRgtrId());
        img.setMdfrId(vo.getRgtrId());

        msgImgMapper.insertMsgImg(img);
    }

    private SuremApiSendRequest buildRequest(MsgSndngVO vo, String kind) {
        return switch (kind) {
            case "T" -> SuremApiSendRequest.builder()
                    .bizType(vo.getBiztype())
                    .senderKey(vo.getSenderKey())
                    .templateCode(vo.getTemplatecode())
                    .to(vo.getCallphone())
                    .text(vo.getMsg())
                    .reqPhone(vo.getReqphone())
                    .reSend("Y")
                    .messageId(vo.getSeqno() != null ? vo.getSeqno().intValue() : null)
                    .build();
            case "R" -> SuremApiSendRequest.builder()
                    .serviceType(vo.getServiceType())
                    .brandKey(vo.getBrandKey())
                    .chatbotId(vo.getChatbotId())
                    .phone(vo.getCallphone())
                    .messagebaseId(vo.getMessagebaseId())
                    .body(vo.getRcsBody())
                    .header("0")
                    .messageId(vo.getSeqno() != null ? vo.getSeqno().intValue() : null)
                    .build();
            case "I" -> SuremApiSendRequest.builder()
                    .country(vo.getCountry())
                    .to(vo.getCallphone())
                    .text(vo.getMsg())
                    .reqPhone(vo.getReqphone())
                    .messageId(vo.getSeqno() != null ? vo.getSeqno().intValue() : null)
                    .build();
            default -> SuremApiSendRequest.builder()  // S, M
                    .to(vo.getCallphone())
                    .text(vo.getMsg())
                    .reqPhone(vo.getReqphone())
                    .subject(vo.getSubject())
                    .messageId(vo.getSeqno() != null ? vo.getSeqno().intValue() : null)
                    .build();
        };
    }

    @Transactional
    public ApiPrnDto send(MsgSndngVO msgVO) {
        ApiPrnDto result                = new ApiPrnDto();
        HashMap<String, Object> bizData = new HashMap<>();

        String normalizedKind = normalizeKind(msgVO.getKind());
        msgVO.setKind(normalizedKind);

        sndngMapper.insertMsgSndng(msgVO);
        saveMsgImagesIfAny(msgVO);

        SuremApiSendRequest  req = buildRequest(msgVO, normalizedKind);
        SuremApiSendResponse res;

        if (testMode) {
            log.info("========= MSG TEST MODE =========");
            log.info("KIND    :::: {}", normalizedKind);
            log.info("TO      :::: {}", msgVO.getCallphone());
            log.info("SUBJECT :::: {}", msgVO.getSubject());
            log.info("MSG     :::: {}", msgVO.getMsg());
            log.info("=================================");
            res = new SuremApiSendResponse();
            res.setCode("1");
            res.setMessage("TEST-MODE");
        } else {
            try {
                String token = client.getToken();
                res = client.send(req, token, normalizedKind);
                log.debug("[MSG] SureM 발송 완료 code={}", res.getCode());
            } catch (WebClientResponseException e) {
                log.error("[MSG] SureM 발송 실패 status={} body={}", e.getStatusCode(), e.getResponseBodyAsString());
                res = new SuremApiSendResponse();
                res.setCode("0");
                res.setMessage(e.getResponseBodyAsString());
            } catch (Exception e) {
                log.error("[MSG] SureM 발송 실패", e);
                res = new SuremApiSendResponse();
                res.setCode("0");
                res.setMessage(e.getMessage());
            }
        }

        // 발송 결과 적재
        MsgRsltVO rslt = new MsgRsltVO();
        rslt.setSeqno(msgVO.getSeqno());
        rslt.setRsltCd(res.getCode());
        rslt.setRsltMsg(res.getMessage());
        rslt.setRgtrId(msgVO.getRgtrId());
        rsltMapper.insertMsgRslt(rslt);

        // ApiPrnDto 세팅
        bizData.put("suremCode",    res.getCode());
        bizData.put("suremMessage", res.getMessage());

        if ("1".equals(res.getCode())) {
            result.setCode("0");
            result.setMsg("메시지 발송이 완료되었습니다.");
        } else {
            result.setCode("-1");
            result.setMsg("메시지 발송에 실패하였습니다. [" + res.getMessage() + "]");
        }

        result.setData(bizData);
        return result;
    }
    /*
    public SuremApiSendResponse send(MsgSndngVO msgVO) {
        String normalizedKind = normalizeKind(msgVO.getKind());
        msgVO.setKind(normalizedKind);

        sndngMapper.insertMsgSndng(msgVO);
        saveMsgImagesIfAny(msgVO);

        SuremApiSendRequest req = buildRequest(msgVO, normalizedKind);
        SuremApiSendResponse res;

        if (testMode) {
            log.info("========= MSG TEST MODE =========");
            log.info("KIND    :::: {}", normalizedKind);
            log.info("TO      :::: {}", msgVO.getCallphone());
            log.info("SUBJECT :::: {}", msgVO.getSubject());
            log.info("MSG     :::: {}", msgVO.getMsg());
            log.info("=================================");
            res = new SuremApiSendResponse();
            res.setCode("1");
            res.setMessage("TEST-MODE");
        } else {
            try {
                String token = client.getToken();
                res = client.send(req, token, normalizedKind);
                log.debug("[MSG] SureM 발송 완료 code={}", res.getCode());
            } catch (WebClientResponseException e) {
                log.error("[MSG] SureM 발송 실패 status={} body={}", e.getStatusCode(), e.getResponseBodyAsString());
                res = new SuremApiSendResponse();
                res.setCode("0");
                res.setMessage(e.getResponseBodyAsString());
            } catch (Exception e) {
                log.error("[MSG] SureM 발송 실패", e);
                res = new SuremApiSendResponse();
                res.setCode("0");
                res.setMessage(e.getMessage());
            }
        }

        MsgRsltVO rslt = new MsgRsltVO();
        rslt.setSeqno(msgVO.getSeqno());
        rslt.setRsltCd(res.getCode());
        rslt.setRsltMsg(res.getMessage());
        rslt.setRgtrId(msgVO.getRgtrId());
        rsltMapper.insertMsgRslt(rslt);

        return res;
    }
    */

    @Transactional(readOnly = true)
    public List<MsgListVO> getMsgSndngList(MsgListVO msgListVO) {
        return sndngMapper.selectMsgList(msgListVO);
    }
}