package kr.or.kids.domain.ca.mail.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.or.kids.domain.ca.mail.client.sensemail.SenseMailResponse;
import kr.or.kids.domain.ca.mail.service.MailService;
import kr.or.kids.domain.ca.mail.vo.MailListVO;
import kr.or.kids.domain.ca.mail.vo.MailSendReqVO;
import kr.or.kids.global.system.common.vo.ApiPrnDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ca/mail")
@Slf4j
public class MailController {

    private final  MailService mailService;

    @Operation( summary = "메일 발송", description = "메일 발송 성공합니다.")
    @ApiResponse(responseCode = "200", description = "메일 발송 성공")
    @PostMapping("/send")
    public ResponseEntity<ApiPrnDto> sendMail(
            @RequestBody MailSendReqVO req
    ) {
        return ResponseEntity.ok(mailService.send(req));
    }
    @Operation( summary = "메일 발송 조회", description = "메일 발송 조회합니다.")
    @ApiResponse(responseCode = "200", description = "메일 발송 조회")
    @GetMapping("/list")
    public ResponseEntity<List<MailListVO>> getMailSndngList(MailListVO mailList) {
        log.info("[MAIL] list request: {}", mailList);
        return ResponseEntity.ok(mailService.getMailSndngList(mailList));
    }
}