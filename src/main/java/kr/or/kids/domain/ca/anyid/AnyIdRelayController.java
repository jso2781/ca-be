package kr.or.kids.domain.ca.anyid;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RestController;

/**
 * AuthResourceRelay.zip(JSP/Servlet) 샘플을 Spring Boot 컨트롤러로 옮긴 구현.
 *
 * config.anyidc.json 에 정의된 accInfo/extract URL을 그대로 제공해야 Any-ID 프론트 모듈이 동작합니다.
 *  - /jsp/esign/accessInfo.jsp
 *  - /jsp/esign/extractInfo.jsp
 *  - /jsp/pid/accessInfo.jsp
 *  - /jsp/pid/extract.jsp
 *  - /jsp/vrs/extract.jsp
 */
@RestController
@Hidden
@Tag(name = "Any-ID(내부)", description = "Any-ID 프론트 모듈이 호출하는 중계형 SDK 내부 엔드포인트입니다. 일반 사용자가 Swagger에서 직접 호출할 API가 아니므로 문서에서 숨깁니다.")
public class AnyIdRelayController {

    private final AnyIdResourcePaths resourcePaths;

    public AnyIdRelayController(AnyIdResourcePaths resourcePaths) {
        this.resourcePaths = resourcePaths;
    }

    // ====== eSign ======
/*
    @RequestMapping(value = "/jsp/esign/accessInfo.jsp", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    public void esignAccessInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        applyCors(request, response);
        if (isOptions(request)) return;

        response.setContentType("application/json");
        new AccessConfigurer(new DefaultEsignAccessor() {})
                .build(
                        resourcePaths.esignProviderKeyJsonFilePath(),
                        request.getInputStream(),
                        response.getWriter()
                );
    }

    @RequestMapping(value = "/jsp/esign/extractInfo.jsp", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    public void esignExtractInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        applyCors(request, response);
        if (isOptions(request)) return;

        final String hashed = request.getHeader("hashed");
        final String clientIP = clientIP(request);

        response.setContentType("application/json");
        new ExtractConfigurer(
                new DefaultEsignExtractor() {
                    @Override
                    public String type() {
                        return "relay"; // install | relay
                    }

                    @Override
                    public boolean isHashed() {
                        return true;
                    }

                    @Override
                    public String hashed() {
                        return hashed;
                    }

                    @Override
                    public kr.or.anyid.auth.extract.IExtractor writeSsobExtensions(Object writable) throws Exception {
                        if (writable instanceof Map<?, ?> m) {
                            ((Map) m).put("clientIp", clientIP);
                        }
                        return this;
                    }
                }
        ).build(
                resourcePaths.kdistApiJsonFilePath(),
                request.getInputStream(),
                response.getWriter()
        );
    }

    // ====== PID (social-relay) ======

    @RequestMapping(value = "/jsp/pid/accessInfo.jsp", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    public void pidAccessInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        applyCors(request, response);
        if (isOptions(request)) return;

        response.setContentType("application/json");
        new AccessConfigurer(
                new DefaultPIDAccessor() {
                    @Override
                    public boolean isStore() {
                        return true;
                    }

                    @Override
                    public kr.or.anyid.auth.access.IAccessor store(Object value) throws Exception {
                        Map<String, Object> anyidSession = new HashMap<>();
                        anyidSession.put("pid", value);
                        session.setAttribute("anyid", anyidSession);
                        return this;
                    }
                }
        ).build(
                resourcePaths.kdistApiJsonFilePath(),
                resourcePaths.pidApiJsonFilePath(),
                request.getInputStream(),
                response.getWriter()
        );
    }

    @RequestMapping(value = "/jsp/pid/extract.jsp", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    public void pidExtract(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        applyCors(request, response);
        if (isOptions(request)) return;

        final String hashed = request.getHeader("hashed");
        final String clientIP = clientIP(request);

        response.setContentType("application/json");
        new ExtractConfigurer(
                new DefaultPIDExtractor() {
                    @Override
                    public boolean isHashed() {
                        return true;
                    }

                    @Override
                    public String hashed() {
                        return hashed;
                    }

                    @Override
                    public boolean isStored() {
                        return true;
                    }

                    @Override
                    public Object retStored() {
                        Map<String, Object> anyid = (Map<String, Object>) session.getAttribute("anyid");
                        if (anyid == null) return null;
                        return anyid.get("pid");
                    }

                    @Override
                    public kr.or.anyid.auth.extract.IExtractor writeSsobExtensions(Object writable) throws Exception {
                        if (writable instanceof Map<?, ?> m) {
                            ((Map) m).put("clientIp", clientIP);
                        }
                        return this;
                    }
                }
        ).build(
                resourcePaths.kdistApiJsonFilePath(),
                request.getInputStream(),
                response.getWriter(),
                session.getId()
        );
    }

    // ====== VRS ======

    @RequestMapping(value = "/jsp/vrs/extract.jsp", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    public void vrsExtract(HttpServletRequest request, HttpServletResponse response) throws Exception {
        applyCors(request, response);
        if (isOptions(request)) return;

        final String hashed = request.getHeader("hashed");
        final String clientIP = clientIP(request);

        response.setContentType("application/json");
        new ExtractConfigurer(
                new DefaultVrsExtractor() {
                    @Override
                    public boolean isHashed() {
                        return true;
                    }

                    @Override
                    public String hashed() {
                        return hashed;
                    }

                    @Override
                    public kr.or.anyid.auth.extract.IExtractor writeSsobExtensions(Object writable) throws Exception {
                        if (writable instanceof Map<?, ?> m) {
                            ((Map) m).put("clientIp", clientIP);
                        }
                        return this;
                    }
                }
        ).build(
                resourcePaths.kdistApiJsonFilePath(),
                request.getInputStream(),
                response.getWriter()
        );
    }

    // ====== helpers ======
    private static boolean isOptions(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    private static void applyCors(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, origin, content-type, accept, hashed");
        String origin = request.getHeader("Origin");
        if (origin != null && !origin.isBlank()) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    private static String clientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("Proxy-Client-IP");
        if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("WL-Proxy-Client-IP");
        if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("HTTP_CLIENT_IP");
        if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) ip = request.getRemoteAddr();
        // X-Forwarded-For 는 "client, proxy1, proxy2" 형태일 수 있음
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
*/
    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
