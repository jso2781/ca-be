package kr.or.kids.global.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import kr.or.kids.domain.ca.auth.service.IdleTokenService;
import kr.or.kids.domain.ca.auth.service.TokenBlacklistService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private IdleTokenService idleTokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {
        http.csrf(csrf -> csrf.disable())
        .cors(withDefaults())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Authorization(인가)
        .authorizeHttpRequests(requests -> requests
            // Any-ID 정적 리소스/설정
            .antMatchers("/anyid/**", "/config/**").permitAll()
            // Auth API
            .antMatchers(HttpMethod.POST, "/api/ca/auth/anyid/login").permitAll()
            .antMatchers(HttpMethod.GET, "/api/ca/auth/me").permitAll()
            .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

            .antMatchers("/api/ca/auth/login", "/api/ca/auth/refresh").permitAll()
            .antMatchers("/api/ca/auth/logout", "/api/ca/auth/extend").authenticated()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().permitAll()
//            .anyRequest().authenticated()
        )
        // Authentication(인증)
        .addFilterBefore(new JwtAuthFilter(jwtTokenProvider, tokenBlacklistService, idleTokenService), UsernamePasswordAuthenticationFilter.class)
        .formLogin(login -> login.disable())
        .httpBasic(basic -> basic.disable())
        .logout(logout -> logout.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173",
                                                       "http://localhost:3000",
                                                       "http://192.168.2.149:30012",
                                                       "http://192.168.2.149:30013",
                                                       "http://192.168.2.149:30020"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 기본 비밀번호 생성을 방지하기 위한 더미 UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }

    /**
     * 비밀번호 인코더 빈 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

