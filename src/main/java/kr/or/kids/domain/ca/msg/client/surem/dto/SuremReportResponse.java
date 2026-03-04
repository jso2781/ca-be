package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Data;
import java.util.List;

@Data
public class SuremReportResponse {
    private String code;
    private String message;
    private String checksum;
    private List<ReportData> data;

    @Data
    public static class ReportData {
        private String result;    // 2: 성공, 4: 실패
        private String member;    // messageId
        private String errorcode;
        private String recvtime;
        private String mediatype; // S/K/L/A/F
        private String sentmedia; // S/M/I/T/R1~R6
    }
}