package kr.or.kids.domain.ca.mail.service;

import kr.or.kids.domain.ca.mail.client.sensemail.SenseMailClient;
import kr.or.kids.domain.ca.mail.client.sensemail.SenseMailRequest;
import kr.or.kids.domain.ca.mail.client.sensemail.SenseMailResponse;
import kr.or.kids.domain.ca.mail.mapper.EmlSndngHistMapper;
import kr.or.kids.domain.ca.mail.mapper.EmlSndngMapper;
import kr.or.kids.domain.ca.mail.vo.EmlSndngHistVO;
import kr.or.kids.domain.ca.mail.vo.EmlSndngVO;
import kr.or.kids.domain.ca.mail.vo.MailListVO;
import kr.or.kids.domain.ca.mail.vo.MailSendReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.test-mode:false}")
    private boolean testMode;

    @Value("${spring.mail.host:}")
    private String mailHost;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Autowired
    public EmlSndngMapper emlSndngMapper;
    @Autowired
    public EmlSndngHistMapper emlSndngHistMapper;
    @Autowired
    public SenseMailClient senseMailClient;


    public void sendMail(String to, String subject, String text) {
        log.info("mail.test-mode = {}", testMode);
        //  테스트 모드면 메일 발송 안 하고 로그만
        if (testMode) {
            log.info("========= MAIL TEST MODE =========");
            log.info("mail.host      :::: {}", mailHost);
            log.info("mail.username  :::: {}", mailUsername);
            log.info("TO             :::: {}", to);
            log.info("SUBJECT        :::: {}", subject);
            log.info("CONTENT        :::: {}", text);
            log.info("==================================");
            return;
        }
        // 실제 발송 (운영용)
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kids@kids.go.kr");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
    /**
     * 단건 메일 발송
     */
    @Transactional
    public SenseMailResponse send(MailSendReqVO req) {

        /* ==================================================
         * 1. 메일 발송 원장 INSERT (대기)
         * ================================================== */
        EmlSndngVO sndng = EmlSndngVO.builder()
                .emlTtl(req.getTitle())
                .emlCn(req.getContent())
                .sndptyFlnm(req.getSenderName())
                .sndptyEmlAddr(req.getSenderEmail())
                .rcvrFlnm(req.getReceiverName())
                .rcvrEmlAddr(req.getReceiverEmail())
                .sndngRsltCd("9") // 대기
                .rgtrId("SYSTEM")
                .build();

        emlSndngMapper.insertEmlSndng(sndng);

        /* ==================================================
         * 2. 센스메일 API 호출
         * ================================================== */
        SenseMailResponse apiRes;
        try {
            apiRes = senseMailClient.send(
                    SenseMailRequest.from(req)
            );
        } catch (Exception e) {
            log.error("[MAIL] 센스메일 발송 실패", e);

            // ❗ 예외 시에도 반드시 응답 객체 생성
            apiRes = new SenseMailResponse();
            apiRes.setResultCode("0");
            apiRes.setErrorMessage(e.getMessage());
        }

        String finalResultCd =
                "1".equals(apiRes.getResultCode()) ? "1" : "0";

        /* ==================================================
         * 3. 발송 이력 INSERT
         * ================================================== */
        EmlSndngHistVO hist = EmlSndngHistVO.builder()
                .emlSndngSn(sndng.getEmlSndngSn())
                .rcvrEmlAddr(req.getReceiverEmail())
                .rcvrFlnm(req.getReceiverName())
                .sndngRsltCd(finalResultCd)
                .sndngTryCnt(1)
                .extSndngRsltCd(apiRes.getResultCode())
                .extMsgId(apiRes.getMessageId())
                .extFailMsg(apiRes.getErrorMessage())
                .rgtrId("SYSTEM")
                .build();

        emlSndngHistMapper.insertHist(hist);

        /* ==================================================
         * 4. 원장 결과 UPDATE
         * ================================================== */
        sndng.setSndngRsltCd(finalResultCd);
        sndng.setMdfrId("SYSTEM");
        emlSndngMapper.updateResult(sndng);

        /* ==================================================
         * 5. 프론트로 내려줄 응답 (중요)
         * ================================================== */
        SenseMailResponse res = new SenseMailResponse();
        res.setResultCode(finalResultCd);
        res.setMessageId(apiRes.getMessageId());
        res.setErrorMessage(apiRes.getErrorMessage());

        return res;   // ✅ 절대 null / 빈 객체 금지
    }
    /**
     * 메일 리스트 조회
     */
    public List<MailListVO> getMailSndngList(MailListVO mailList) {

        return emlSndngHistMapper.selectMailSendList(mailList);
    }
}

