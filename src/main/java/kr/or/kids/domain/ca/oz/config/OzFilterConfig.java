package kr.or.kids.domain.ca.oz.config;

import kr.or.kids.domain.ca.oz.filter.CorsHeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * OZ 서블릿을 위한 필터 등록 설정
 */
@Configuration
public class OzFilterConfig {

    /**
     * CORS 필터를 /oz90/* 경로에 적용
     */
    @Bean
    public FilterRegistrationBean<CorsHeaderFilter> ozCorsFilter() {
        FilterRegistrationBean<CorsHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(new CorsHeaderFilter());
        registrationBean.addUrlPatterns("/oz90/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.setName("ozCorsHeaderFilter");
        
        return registrationBean;
    }
}
