package kr.or.kids.domain.ca.anyid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.kids.domain.ca.anyid.dto.AnyIdLoginRequest;
import kr.or.kids.domain.ca.anyid.dto.AnyIdLoginResponse;

@RestController
@RequestMapping("/api/ca/auth")
@Tag(
        name = "인증",
        description = "세션(JSESSIONID) 기반 인증 API입니다. Any-ID 인증이 성공하면 프론트에서 ssob/tag를 받아 /api/auth/anyid/login 으로 전달하고, 서버가 세션을 생성합니다. 이후 요청은 Authorization 헤더가 아니라 쿠키(JSESSIONID)를 사용합니다."
)
public class AnyIdAuthController {

    private final AnyIdAuthService anyIdAuthService;

    public AnyIdAuthController(AnyIdAuthService anyIdAuthService) {
        this.anyIdAuthService = anyIdAuthService;
    }

    /**
     * React 로그인 페이지에서 Any-ID 인증 성공 후 ssob/tag를 전달받아 서버에서 검증/복호화하고,
     * 세션 기반으로 로그인 처리합니다.
     */
    @PostMapping("/anyid/login")
    @Operation(
            summary = "Any-ID 인증 결과로 세션 로그인",
            description = "Any-ID 화면에서 인증 성공 후 전달된 ssob/tag를 검증/복호화하여 사용자 정보를 추출하고, Spring Security 세션을 생성합니다. 브라우저는 Set-Cookie로 내려오는 JSESSIONID를 유지해야 하며, 프론트(React)는 axios/fetch에서 withCredentials를 사용해야 합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검증/추출 성공 및 세션 생성", content = @Content(schema = @Schema(implementation = AnyIdLoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 누락 또는 형식 오류"),
            @ApiResponse(responseCode = "401", description = "ssob 검증 실패(위변조/만료 등)"),
            @ApiResponse(responseCode = "500", description = "SDK 처리 오류")
    })
    public ResponseEntity<AnyIdLoginResponse> anyidLogin(
            @RequestBody AnyIdLoginRequest req,
            HttpServletRequest httpRequest
    ) {
        AnyIdLoginResponse extracted = anyIdAuthService.verifyAndExtract(req);

        // 최소 식별키는 CI (연계정보) 사용 권장 (가이드에서 CI 정의) 
        // - CI는 본인확인기관에서 본인을 식별하기 위해 생성한 연계정보
        // - ssob 내 제공 항목(인증수단별 제공항목) 중 ci가 존재
        String principal = extracted.ci() != null ? extracted.ci() : (extracted.name() != null ? extracted.name() : "anyid-user");

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                "N/A",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return ResponseEntity.ok(extracted);
    }

    @GetMapping("/me")
    @Operation(
            summary = "현재 세션 인증 상태 조회",
            description = "세션(JSESSIONID) 기준으로 현재 로그인 여부와 principal/권한을 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<?> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.ok(java.util.Map.of("authenticated", false));
        }
        return ResponseEntity.ok(java.util.Map.of(
                "authenticated", true,
                "principal", auth.getPrincipal(),
                "authorities", auth.getAuthorities()
        ));
    }

    /*
    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃(세션 무효화)",
            description = "현재 세션을 invalidate하고 SecurityContext를 정리합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(java.util.Map.of("status", "ok"));
    }

     */
}
