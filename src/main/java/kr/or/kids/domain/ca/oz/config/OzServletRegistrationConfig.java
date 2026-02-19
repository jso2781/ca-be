package kr.or.kids.domain.ca.oz.config;

import kr.or.kids.domain.ca.oz.servlet.OzServletWrapper;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OZ 리포트 서블릿 등록 설정
 * OZ 서버와 통신하기 위한 서블릿을 Spring Boot에 등록합니다.
 */
@Configuration
public class OzServletRegistrationConfig {
	
	/**
	 * OZServlet을 /oz90/server 경로로 등록
	 * OZ 리포트 뷰어와 서버 간 통신을 위한 엔드포인트를 제공합니다.
	 * CORS 지원을 위해 OzServletWrapper를 사용합니다.
	 * 
	 * @return ServletRegistrationBean OZ 서블릿 등록 빈
	 */
	@Bean
	public ServletRegistrationBean<OzServletWrapper> ozServletRegistration() {
	
		ServletRegistrationBean<OzServletWrapper> registrationBean = 
			new ServletRegistrationBean<>(new OzServletWrapper());
		
		registrationBean.addUrlMappings("/oz90/server");
		registrationBean.addUrlMappings("/oz90/server/*");
		
		// OZ 서버 홈 디렉토리를 빌드된 리소스 경로로 설정
		// Gradle 빌드: build/resources/main
		// 실행 중에는 클래스패스에서 자동으로 찾음
		String ozHome = System.getProperty("user.dir") + "/build/resources";
		registrationBean.addInitParameter("OZSERVER_HOME", ozHome);
		
		return registrationBean;
	}

}
