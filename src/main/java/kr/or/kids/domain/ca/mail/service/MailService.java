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
import kr.or.kids.global.system.common.vo.ApiPrnDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
    public ApiPrnDto send(MailSendReqVO req) {
        ApiPrnDto result                = new ApiPrnDto();
        HashMap<String, Object> bizData = new HashMap<>();
        /* ==================================================
         * 1. 메일 발송 원장 INSERT (대기)
         * ================================================== */
        EmlSndngVO sndng = EmlSndngVO.builder()
                .emlTtl(req.getEmlTtl())
                .emlCn(req.getEmlCn())
                .sndptyFlnm(req.getSndptyFlnm())
                .sndptyEmlAddr(req.getSndptyEmlAddr())
                .rcvrFlnm(req.getRcvrFlnm())
                .rcvrEmlAddr(req.getRcvrEmlAddr())
                .sndngRsltCd("9") // 대기
                .rgtrId("SYSTEM")
                .mdfrId("SYSTEM")
                .build();

        emlSndngMapper.insertEmlSndng(sndng);

        /* ==================================================
         * 2. 센스메일 API 호출
         * ================================================== */
        /*SenseMailResponse apiRes;
        try {
            apiRes = senseMailClient.send(SenseMailRequest.from(req));
        } catch (Exception e) {
            log.error("[MAIL] 센스메일 발송 실패", e);

            // ❗ 예외 시에도 반드시 응답 객체 생성
            apiRes = new SenseMailResponse();
            apiRes.setResultCode("0");
            apiRes.setErrorMessage(e.getMessage());
        }

        String finalResultCd ="1".equals(apiRes.getResultCode()) ? "1" : "0";
*/
        /*SenseMailResponse apiRes;

        if (testMode) {

            apiRes = new SenseMailResponse();
            apiRes.setCode("1");           // 성공으로 간주
            apiRes.setMsg("메일 발송 성공 하였습니다.");
            apiRes.setData(bizData);

        } else {
            try {
                apiRes = senseMailClient.send(SenseMailRequest.from(req));
            } catch (Exception e) {
                log.error("[MAIL] 센스메일 발송 실패", e);
                apiRes = new SenseMailResponse();
                apiRes.setCode("0");
                apiRes.setErrMsg(e.getMessage());
            }
        }
        String finalResultCd ="1".equals(apiRes.getCode()) ? "1" : "0";*/
        /* ==================================================
         * 2. SMTP 메일 발송
         * ================================================== */
        String resultCd;
        String failMsg = null;
        String msg = null;
        String rcvrEmlAddr;

        try {
            if (testMode) {
                // 테스트 모드: 실제 발송 없이 로그만
                log.info("========= MAIL TEST MODE =========");
                log.info("TO      :::: {}", req.getRcvrEmlAddr());
                log.info("SUBJECT :::: {}", req.getEmlTtl());
                log.info("CONTENT :::: {}", req.getEmlCn());
                log.info("==================================");
            } else {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(req.getSndptyEmlAddr());
                message.setTo(req.getRcvrEmlAddr());
                message.setSubject(req.getEmlTtl());
                message.setText(req.getEmlCn());
                mailSender.send(message);
            }
            resultCd = "1"; // 성공
            msg = "메시지 발송 성공하였습니다.";
            rcvrEmlAddr = req.getRcvrEmlAddr();

        } catch (Exception e) {
            log.error("[MAIL] SMTP 발송 실패", e);
            resultCd = "0"; // 실패
            failMsg = e.getMessage();
        }
        /* ==================================================
         * 3. 발송 이력 INSERT
         * ================================================== */
        EmlSndngHistVO hist = EmlSndngHistVO.builder()
                .emlSndngSn(sndng.getEmlSndngSn())
                .rcvrEmlAddr(req.getRcvrEmlAddr())
                .rcvrFlnm(req.getRcvrFlnm())
                .sndngRsltCd(resultCd)
                .sndngTryCnt(1)
                .otsdEmlOrgnlRsltCd(resultCd)
                .otsdEmlMsgId(null)
                .otsdEmlErrMsgCn(failMsg)
                .rgtrId("SYSTEM")
                .mdfrId("SYSTEM")
                .build();

        emlSndngHistMapper.insertHist(hist);

        /* ==================================================
         * 4. 원장 결과 UPDATE
         * ================================================== */
        sndng.setSndngRsltCd(resultCd);
        sndng.setMdfrId("SYSTEM");
        emlSndngMapper.updateResult(sndng);

        /* ==================================================
         * 5. 내려줄 응답
         * ================================================== */
        SenseMailResponse res = new SenseMailResponse();
        res.setCode(resultCd);
        res.setMsg(msg);
        res.setErrMsg(failMsg);
        res.setData(bizData);

        // ApiPrnDto 세팅
        bizData.put("code",    res.getCode());
        bizData.put("message", res.getMsg());
        bizData.put("rcvrEmlAddr", req.getRcvrEmlAddr());
        if ("1".equals(res.getCode())) {
            result.setCode("1");
            result.setMsg("메시지 발송이 완료되었습니다.");
        } else {
            result.setCode("-1");
            result.setMsg("메시지 발송에 실패하였습니다. [" + res.getMsg() + "]");
        }

        result.setData(bizData);
        return result;
    }
    /**
     * 메일 리스트 조회
     */
    public List<MailListVO> getMailSndngList(MailListVO mailList) {

        return emlSndngHistMapper.selectMailSendList(mailList);
    }
}

