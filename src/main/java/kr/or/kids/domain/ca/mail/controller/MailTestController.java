/*
package kr.go.kids.domain.ca.mail.controller;

import kr.go.kids.domain.ca.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class MailTestController {

    private final MailService mailService;

    @GetMapping("/test")
    public String test() {

        mailService.sendMail(
                "receiver@kids.go.kr",
                "센스메일 연동 테스트",
                "내부망 센스메일 SMTP 연동 성공"
        );


        return "MAIL SEND OK";
    }
}*/
