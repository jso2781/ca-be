package kr.or.kids.domain.ca.msg.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.or.kids.domain.ca.msg.service.MessageService;
import kr.or.kids.domain.ca.msg.vo.MsgListVO;
import kr.or.kids.domain.ca.msg.vo.MsgSndngVO;
import kr.or.kids.domain.ca.msg.client.surem.dto.SuremApiSendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ca/msg")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    /**
     * 메시지 발송 요청
     * - 현재는 DB 적재 + MOCK
     */
    @Operation( summary = "메시지 발송", description = "메시지 발송 성공합니다.")
    @ApiResponse(responseCode = "200", description = "메시지 발송 성공")
    @PostMapping("/send")
    public ResponseEntity<SuremApiSendResponse> sendMessage(@RequestBody MsgSndngVO msgVO) {

        log.info("[MSG] send request: {}", msgVO);

        SuremApiSendResponse res = messageService.send(msgVO);

        return ResponseEntity.ok(res);
    }
    @Operation( summary = "메시지 발송 조회", description = "메시지 발송 조회합니다.")
    @ApiResponse(responseCode = "200", description = "메시지 발송 조회")
    @GetMapping("/list")
    public ResponseEntity<List<MsgListVO>> getMsgSndngList(MsgListVO msgListVO) {
        log.info("[MSG] list request: {}", msgListVO);
        return ResponseEntity.ok(messageService.getMsgSndngList(msgListVO));
    }

}
