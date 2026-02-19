package kr.or.kids.domain.ca.adr.controller;

import kr.or.kids.domain.ca.adr.vo.JusoResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/adr")
@Slf4j
public class AdrCallbackController {

    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public String callback(JusoResultVO juso, Model model) {

        log.info("주소 콜백 수신: {}", juso);
        model.addAttribute("juso", juso);

        return "jusoCallback";
    }
}
