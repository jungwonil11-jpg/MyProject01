package com.study.myproject01.common.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    // application.yaml에 존재하는 값 가져온다.
    @Value("${jwt.시크릿키}")
    private String secret;

    @Value("${jwt.액세스토큰시간}")
    private long accessTokenValidity;

    @Value("${jwt.리프레시토큰시간}")
    private long refreshTokenValidity;

    @Value
    public JwtUtil jwtUtil() {
        return new JwtUtil(secret, accessTokenValidity, refreshTokenValidity);
    }

}

