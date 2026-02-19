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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MsgSndngMapper sndngMapper;
    private final MsgRsltMapper rsltMapper;
    private final MsgImgMapper msgImgMapper;
    private final SuremMessageClient client;

    /* =====================
       KIND 처리
       ===================== */
    private String normalizeKind(String kind) {
        if (kind == null) return null;
        return switch (kind) {
            case "SMS" -> "S";
            case "LMS", "MMS" -> "M";
            case "KAKAO", "TALK" -> "T";
            case "INTL" -> "I";
            case "RCS" -> "R";
            case "S", "M", "T", "I", "R" -> kind;
            default -> throw new IllegalArgumentException("유효하지 않은 KIND: " + kind);
        };
    }

    private String convertToSuremKind(String kind) {
        return switch (kind) {
            case "S" -> "SMS";
            case "M" -> "LMS";
            case "T" -> "KAKAO";
            case "I" -> "INTL";
            case "R" -> "RCS";
            default -> throw new IllegalArgumentException("유효하지 않은 API KIND: " + kind);
        };
    }

    /* =====================
       이미지 저장
       ===================== */
    private void saveMsgImagesIfAny(MsgSndngVO vo) {

        if (vo.getEtc1() == null
                && vo.getEtc2() == null
                && vo.getEtc3() == null) {
            return;
        }

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

    /* =====================
       메인 발송
       ===================== */
    @Transactional
    public SuremApiSendResponse send(MsgSndngVO msgVO) {

        // 1. KIND 정규화
        String normalizedKind = normalizeKind(msgVO.getKind());
        msgVO.setKind(normalizedKind);

        // 2. 발송요청 저장
        sndngMapper.insertMsgSndng(msgVO);

        // 3. 이미지 저장
        saveMsgImagesIfAny(msgVO);

        // 4. Surem 요청 생성
        SuremApiSendRequest req = new SuremApiSendRequest();
        req.setUsercode(msgVO.getUsercode());
        req.setBiztype(msgVO.getBiztype());
        req.setReqname(msgVO.getReqname());
        req.setReqphone(msgVO.getReqphone());
        req.setCallname(msgVO.getCallname());
        req.setCallphone(msgVO.getCallphone());
        req.setSubject(msgVO.getSubject());
        req.setMsg(msgVO.getMsg());
        req.setKind(convertToSuremKind(normalizedKind));

        try {
            // 5. Surem 호출 (현재 MOCK)
            // SuremApiSendResponse res = client.send(req);

            SuremApiSendResponse res = new SuremApiSendResponse();
            res.setResult("MOCK");
            res.setErrmsg("API 호출 생략");

            // 6. 결과 저장
            MsgRsltVO rslt = new MsgRsltVO();
            rslt.setSeqno(msgVO.getSeqno());
            rslt.setRsltCd(res.getResult());
            rslt.setRsltMsg(res.getErrmsg());
            rslt.setRgtrId(msgVO.getRgtrId());

            rsltMapper.insertMsgRslt(rslt);

            return res;

        } catch (WebClientResponseException e) {

            MsgRsltVO rslt = new MsgRsltVO();
            rslt.setSeqno(msgVO.getSeqno());
            rslt.setRsltCd("F");
            rslt.setRsltMsg(e.getResponseBodyAsString());
            rslt.setRgtrId(msgVO.getRgtrId());

            rsltMapper.insertMsgRslt(rslt);
            throw e;
        }
    }
    @Transactional(readOnly = true)
    public List<MsgListVO> getMsgSndngList(MsgListVO msgListVO) {
        return sndngMapper.selectMsgList(msgListVO);
    }
}
