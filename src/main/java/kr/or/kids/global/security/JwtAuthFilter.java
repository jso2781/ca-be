package kr.or.kids.global.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import kr.or.kids.domain.ca.auth.service.IdleTokenService;
import kr.or.kids.domain.ca.auth.service.TokenBlacklistService;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final IdleTokenService idleTokenService;

    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider, TokenBlacklistService tokenBlacklistService, IdleTokenService idleTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenBlacklistService = tokenBlacklistService;
        this.idleTokenService = idleTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String bearer = request.getHeader("Authorization");

        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")){
            String token = bearer.substring(7);

            try{
                // 1) 로그아웃된 토큰 즉시 차단
                if(tokenBlacklistService.isBlacklisted(token)){
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // 2) ✅ tokenSn Claim 추출
                String tokenSn = jwtTokenProvider.getTokenSn(token);

                // 3) ✅ idle 만료 체크: key 없으면(30분 idle) 408 (request idle)
                if (!idleTokenService.exists(tokenSn)) {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
                    return;
                }

                // 4) ✅ 활동 감지: TTL 30분으로 리셋
                idleTokenService.touch(tokenSn);

                // 5) JWT 로그인 인증 처리, JWT 토큰 문자열에서 회원ID(mbrId) 가져옴.
                String mbrId = jwtTokenProvider.getSubject(token);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(mbrId, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }catch(Exception e){
                SecurityContextHolder.clearContext();
                // 여기서 401로 끊을지, 그냥 체인 계속 태울지는 정책 선택
                // 보통은 그냥 체인 진행(뒤에서 인증 실패 처리)해도 됩니다.
            }
        }

        filterChain.doFilter(request, response);
    }
}
