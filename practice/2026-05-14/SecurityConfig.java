package com.study.myproject01.config;

import com.study.myproject01.common.jwt.JwtRequestFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// Configuration : 설정 클래스 (Spring boot 실행 될 때 같이 실행 된다.)
// EnableWebSecurity : Spring Security에서 웹 보안 설정을 활성화 하는 어노테이션
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    // 생성자에 JwtRequestFilter 를 주입
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }



    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        log.info("securityFilterChain 시작");
        http
                // 1. CORS 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF 보호 비활성 ( JWT 토큰 사용 시 일반적으로 비활성
                .csrf(csrf -> csrf.disable())
                // 세션 생성 안함, JWT 사용 시 권장
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 요청별 권한 설정
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 로그인/회원가입/리프레시는 인증 없이 허용
                        .requestMatchers("/members/login", "/members/register", "/members/refresh").permitAll()
                        // 방명록 전체 허용
                        .requestMatchers("/guestbook/list").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"success\":false, \"message\":\"인증이 필요합니다\"}");
                        })
                )

                // JWT 필터
                // UsernamePasswordAuthenticationFilter 보다 앞에 삽입
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 허용할 Origin, 메서드, 헤더, 인증
        // 리액트
        corsConfiguration.setAllowedOrigins(Arrays.asList("_____"));
        corsConfiguration.setAllowedMethods(Arrays.asList("_____", "_____", "_____", "_____", "_____"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("_____"));
        corsConfiguration.setAllowCredentials(_____);

        // URL 패턴 기반으로 CORS 설정을 관리하는 저장소 생성
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        // 모든 URL 경로에 적용
        urlBasedCorsConfigurationSource.registerCorsConfiguration("_____", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}

