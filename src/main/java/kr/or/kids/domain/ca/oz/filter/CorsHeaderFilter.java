package kr.or.kids.domain.ca.oz.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * OZ 리포트 서블릿을 위한 CORS 헤더 필터
 * Cross-Origin Resource Sharing (CORS) 정책을 처리합니다.
 * 
 * 이 필터는 OzFilterConfig에서 FilterRegistrationBean으로 등록됩니다.
 */
public class CorsHeaderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 시 필요한 로직 (javax.servlet.Filter 인터페이스 구현)
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        if (origin != null && !origin.isEmpty()) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Vary", "Origin");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, Cache-Control");
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        // Preflight 요청(OPTIONS)인 경우 응답만 보내고 체인 중단
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        // 필터 종료 시 필요한 로직
    }
}
