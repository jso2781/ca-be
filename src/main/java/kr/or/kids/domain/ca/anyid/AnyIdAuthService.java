package kr.or.kids.domain.ca.anyid;

import kr.or.kids.domain.ca.anyid.dto.AnyIdLoginRequest;
import kr.or.kids.domain.ca.anyid.dto.AnyIdLoginResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import kr.or.anyid.auth.AnyidAuth;
import kr.or.anyid.util.AnyidCertRef;
import org.springframework.stereotype.Service;

@Service
public class AnyIdAuthService {

    private final AnyIdResourcePaths resourcePaths;
    private final ObjectMapper objectMapper;

    public AnyIdAuthService(AnyIdResourcePaths resourcePaths, ObjectMapper objectMapper) {
        this.resourcePaths = resourcePaths;
        this.objectMapper = objectMapper;
    }

    /**
     * Any-ID SDK 중계형: 프론트에서 전달된 ssob(인증정보) + tag(tx) 를 복호화/검증하고,
     * 필요한 사용자 정보를 추출합니다.
     */
    public AnyIdLoginResponse verifyAndExtract(AnyIdLoginRequest req) {
        if (req == null || req.ssob() == null || req.ssob().isBlank() || req.tag() == null || req.tag().isBlank()) {
            throw new IllegalArgumentException("ssob and tag are required");
        }

        // orgLogin.jsp 샘플 로직과 동일한 흐름

        String ssobStr = null;
        AnyidAuth anyidAuth = new AnyidAuth();
        AnyidCertRef anyidCertRef = new AnyidCertRef();
		try {
			Map<String, Object> resultMap = anyidCertRef.decryptSsob(req.ssob(), req.tag(), resourcePaths.kdistApiJsonFilePath());
	        ssobStr = (String) resultMap.get("ssobStr");
	        if (ssobStr == null || ssobStr.isBlank()) {
	            throw new IllegalStateException("decryptSsob did not return ssobStr");
	        }

	        Map<String, Object> ssob = readJsonMap(ssobStr);

	        String ci = asString(ssob.get("ci"));
	        String name = asString(ssob.get("name"));
	        Integer authLvl = asInt(ssob.get("authLvl"));
	        String group = asString(ssob.get("group"));
	        String timestamp = asString(ssob.get("timestamp"));
	        String clientIp = asString(ssob.get("clientIp"));

	        return new AnyIdLoginResponse(
	                "success",
	                ci,
	                name,
	                authLvl,
	                group,
	                timestamp,
	                clientIp,
	                Map.of(
	                        "decrypt", resultMap,
	                        "ssob", ssob
	                )
	        );
		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
	        return new AnyIdLoginResponse(
	                "fail",
	                null,
	                null,
	                null,
	                null,
	                null,
	                null,
	                Map.of(
	                        "decrypt", null /* resultMap */,
	                        "ssob", null /* ssob */
	                )
	        );
		}
    }

    private Map<String, Object> readJsonMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse ssobStr JSON", e);
        }
    }

    private static String asString(Object v) {
        return v == null ? null : String.valueOf(v);
    }

    private static Integer asInt(Object v) {
        if (v == null) return null;
        if (v instanceof Number n) return n.intValue();
        try {
            return Integer.parseInt(String.valueOf(v));
        } catch (Exception e) {
            return null;
        }
    }
}
