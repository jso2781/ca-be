package kr.or.kids.domain.ca.oz.servlet;

import oz.server.OZServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OZServlet을 래핑하여 CORS 헤더를 추가하는 서블릿
 */
public class OzServletWrapper extends OZServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // CORS 헤더 추가
        String origin = request.getHeader("Origin");
        if (origin != null && !origin.isEmpty()) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Vary", "Origin");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, Cache-Control, X-Requested-With");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age", "3600");
        }
        
        // Preflight 요청 처리
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        // 실제 OZServlet 로직 실행
        super.service(request, response);
    }
}
