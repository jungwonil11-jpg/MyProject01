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
    public SecurityConfig(_____ jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }



    _____
    _____ securityFilterChain(_____ http, _____ corsConfigurationSource) throws _____ {
        log._____(_____);
        http
                // 1. CORS 설정 (corsConfigurationSource() 별도로 생성
                .____(cors -> cors._____(corsConfigurationSource()))
                // CSRF 보호 비활성 ( JWT 토큰 사용 시 일반적으로 비활성
                .____(csrf -> csrf._____())
                // 세션 생성 안함, JWT 사용 시 권장
                ._____(session -> session._____(SessionCreationPolicy.STATELESS))
                // 요청별 권한 설정
                ._____(authorizeRequests -> authorizeRequests
                        // 허용한 URL만 통과 시킨다.
                        ._____(_____, _____, _____)._____()
                        ._____(_____)._____()
                        ._____()._____())
                ._____(e -> e
                        ._____((request, response, authException) -> {
                            response._____(HttpServletResponse.SC_UNAUTHORIZED);
                            response._____(_____);
                            response._____()._____(_____);
                        })
                )

                // JWT 필터: UsernamePasswordAuthenticationFilter 보다 앞에 삽입
                ._____(jwtRequestFilter, _____.class);

        return http._____();
    }

    _____
    _____ corsConfigurationSource() {
        _____ corsConfiguration = _____ _____();

        // 허용할 Origin, 메서드, 헤더, 인증
        corsConfiguration._____(_____._____(_____));
        corsConfiguration._____(_____._____(_____, _____, _____, _____, _____));
        corsConfiguration._____(_____._____(_____));
        corsConfiguration._____(true);

        // URL 패턴 기반으로 CORS 설정을 관리하는 저장소 생성
        _____ urlBasedCorsConfigurationSource = _____ _____();

        // 모든 URL 경로에 적용, CORS 규칙 객체
        urlBasedCorsConfigurationSource._____(_____, corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}
