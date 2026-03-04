package kr.or.kids.domain.ca.msg.controller;

import kr.or.kids.domain.ca.msg.client.surem.dto.*;
import kr.or.kids.domain.ca.msg.service.*;
import kr.or.kids.domain.ca.msg.vo.MsgListVO;
import kr.or.kids.domain.ca.msg.vo.MsgSndngVO;
import kr.or.kids.global.system.common.vo.ApiPrnDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ca/msg")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final ReportService reportService;
    private final OptOutService optOutService;
    private final TemplateService templateService;
    private final ProfileService profileService;
    private final CampaignService campaignService;

    /* =====================================================================
       메시지 발송
       ===================================================================== */

    /** 메시지 발송 */
    @PostMapping("/send")
    public ResponseEntity<ApiPrnDto> send(@RequestBody MsgSndngVO req) {
        return ResponseEntity.ok(messageService.send(req));
    }
/*
    public ResponseEntity<SuremApiSendResponse> send(@RequestBody MsgSndngVO req) {
        return ResponseEntity.ok(messageService.send(req));
    }
*/

    /** 메시지 목록 조회 */
    @GetMapping("/list")
    public ResponseEntity<List<MsgListVO>> list( MsgListVO req) {
        return ResponseEntity.ok(messageService.getMsgSndngList(req));
    }

    /* =====================================================================
       전송결과 Polling
       ===================================================================== */

    /** 결과 조회 + 완료처리 */
    @GetMapping("/report")
    public ResponseEntity<SuremReportResponse> getReport(@RequestParam String type) {
        return ResponseEntity.ok(reportService.getReportAndComplete(type));
    }

    /** 완료처리만 */
    @PostMapping("/report/complete")
    public ResponseEntity<SuremBaseResponse> completeReport(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(reportService.completeReport(req.get("checksum")));
    }

    /* =====================================================================
       수신거부
       ===================================================================== */

    /** 수신거부 조회 + 마킹 */
    @GetMapping("/optout")
    public ResponseEntity<SuremOptOutResponse> getOptOut() {
        return ResponseEntity.ok(optOutService.getAndMarkOptOutList());
    }

    /** 수신거부 마킹만 */
    @PostMapping("/optout/mark")
    public ResponseEntity<SuremOptOutMarkResponse> markOptOut(@RequestBody Map<String, List<Integer>> req) {
        return ResponseEntity.ok(optOutService.markOptOut(req.get("idList")));
    }

    /* =====================================================================
       알림톡 템플릿
       ===================================================================== */

    /** 템플릿 조회 */
    @GetMapping("/template")
    public ResponseEntity<SuremTemplateResponse> getTemplate(
            @RequestParam String senderKey,
            @RequestParam String templateCode) {
        return ResponseEntity.ok(templateService.getTemplate(senderKey, templateCode));
    }

    /** 템플릿 리스트 조회 */
    @GetMapping("/template/list")
    public ResponseEntity<SuremTemplateListResponse> getTemplateList(
            @RequestParam String senderKey,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int count) {
        return ResponseEntity.ok(templateService.getTemplateList(senderKey, page, count));
    }

    /** 템플릿 등록 */
    @PostMapping("/template/create")
    public ResponseEntity<SuremBaseResponse> createTemplate(@RequestBody Map<String, Object> req) {
        return ResponseEntity.ok(templateService.createTemplate(req));
    }

    /** 템플릿 수정 */
    @PostMapping("/template/update")
    public ResponseEntity<SuremBaseResponse> updateTemplate(@RequestBody Map<String, Object> req) {
        return ResponseEntity.ok(templateService.updateTemplate(req));
    }

    /** 템플릿 검수 요청 */
    @PostMapping("/template/request")
    public ResponseEntity<SuremBaseResponse> requestTemplate(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(templateService.requestTemplate(
                req.get("senderKey"), req.get("templateCode")));
    }

    /** 템플릿 검수 요청 취소 */
    @PostMapping("/template/request/cancel")
    public ResponseEntity<SuremBaseResponse> cancelTemplateRequest(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(templateService.cancelTemplateRequest(
                req.get("senderKey"), req.get("templateCode")));
    }

    /** 템플릿 삭제 */
    @PostMapping("/template/delete")
    public ResponseEntity<SuremBaseResponse> deleteTemplate(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(templateService.deleteTemplate(
                req.get("senderKey"), req.get("templateCode")));
    }

    /** 템플릿 휴면 해제 */
    @PostMapping("/template/dormant/release")
    public ResponseEntity<SuremBaseResponse> releaseTemplateDormant(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(templateService.releaseTemplateDormant(
                req.get("senderKey"), req.get("templateCode")));
    }

    /** 템플릿 카테고리 조회 */
    @GetMapping("/template/category")
    public ResponseEntity<SuremBaseResponse> getTemplateCategory() {
        return ResponseEntity.ok(templateService.getTemplateCategory());
    }

    /* =====================================================================
       카카오 발신프로필
       ===================================================================== */

    /** 채널 인증 토큰 요청 */
    @PostMapping("/profile/token")
    public ResponseEntity<SuremBaseResponse> requestProfileToken(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(profileService.requestProfileToken(
                req.get("yellowId"), req.get("phoneNumber")));
    }

    /** 발신프로필 키 발급 */
    @PostMapping("/profile/create")
    public ResponseEntity<SuremProfileCreateResponse> createProfile(@RequestBody Map<String, Object> req) {
        return ResponseEntity.ok(profileService.createProfile(req));
    }

    /** 발신프로필 리스트 조회 */
    @GetMapping("/profile/list")
    public ResponseEntity<SuremBaseResponse> getProfileList() {
        return ResponseEntity.ok(profileService.getProfileList());
    }

    /** 발신프로필 상세 조회 */
    @GetMapping("/profile")
    public ResponseEntity<SuremBaseResponse> getProfile(@RequestParam String senderKey) {
        return ResponseEntity.ok(profileService.getProfile(senderKey));
    }

    /** 프로필 휴면 해제 */
    @PostMapping("/profile/dormant/release")
    public ResponseEntity<SuremBaseResponse> releaseProfileDormant(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(profileService.releaseProfileDormant(req.get("senderKey")));
    }

    /** 수신거부 번호 업데이트 */
    @PostMapping("/profile/unsubscribe")
    public ResponseEntity<SuremBaseResponse> updateUnsubscribe(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(profileService.updateUnsubscribe(
                req.get("senderKey"),
                req.get("unsubscribePhone"),
                req.get("unsubscribeAuth")));
    }

    /* =====================================================================
       캠페인
       ===================================================================== */

    /** 캠페인 생성 */
    @PostMapping("/campaign/create")
    public ResponseEntity<SuremCampaignResponse> createCampaign(@RequestBody SuremCampaignRequest req) {
        return ResponseEntity.ok(campaignService.createCampaign(req));
    }
}