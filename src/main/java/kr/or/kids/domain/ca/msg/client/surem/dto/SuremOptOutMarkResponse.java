package kr.or.kids.domain.ca.msg.client.surem.dto;

import lombok.Data;
import java.util.List;

@Data
public class SuremOptOutMarkResponse {
    private String code;
    private String message;
    private List<Integer> data; // 실패한 ID 리스트
}