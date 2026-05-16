# 연습 회차 — SecurityConfig 67%

## 회차 정보
- **파일**: SecurityConfig.java (단일 파일 모드)
- **난이도**: 67%

## 빈칸 카테고리 (P1~P7)
- **P2 setter 첫 등장**: `setAllowedOrigins`, `setAllowedMethods`, `setAllowedHeaders`, `setAllowCredentials`
- **P3 표준 라이브러리 메소드 호출**: HttpSecurity 체이닝 (`cors`, `csrf`, `sessionManagement`, `authorizeHttpRequests`, `exceptionHandling`, `addFilterBefore`, `build`), `requestMatchers`/`permitAll`/`anyRequest`/`authenticated`, `authenticationEntryPoint`, `setStatus`/`setContentType`/`getWriter`/`write`, `log.info`, `Arrays.asList`, `registerCorsConfiguration`, `configurationSource`, `sessionCreationPolicy`, `disable`
- **P4 어노테이션**: `@Bean` × 2
- **P5 같은 토큰 중복**: `Arrays`/`asList`/`requestMatchers`/`permitAll` 2번째 이후
- **P6 문자열 리터럴**: URL 패턴 (`/members/login`, `/members/register`, `/members/refresh`, `/guestbook/**`, `/**`), CORS 허용 origin/method/header, 응답 메시지
- **P7 변수 타입 / 생성자 / 시그니처 리턴 타입**: `JwtRequestFilter`, `HttpSecurity`, `CorsConfigurationSource`, `SecurityFilterChain`, `Exception`, `CorsConfiguration`, `UrlBasedCorsConfigurationSource`, `UsernamePasswordAuthenticationFilter`, `new CorsConfiguration()`, `new UrlBasedCorsConfigurationSource()`

## 살아있는 것 (67%라서)
- 변수명 (`http`, `corsConfiguration`, `urlBasedCorsConfigurationSource`)
- 메소드명 (`securityFilterChain`, `corsConfigurationSource`)
- `public` 접근제어자
- 값 토큰 (P8): `return`, `true`, `.class`, `SessionCreationPolicy.STATELESS`, `HttpServletResponse.SC_UNAUTHORIZED`

## 학습 포인트
1. `@EnableWebSecurity` + `@Bean SecurityFilterChain` — Spring Security 6.x의 람다 DSL 방식 보안 설정
2. CORS / CSRF / 세션 정책 — JWT 환경에서 왜 STATELESS, csrf disable인지
3. `requestMatchers(...).permitAll()` — 화이트리스트 URL 패턴
4. `addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)` — JWT 필터 삽입 위치
5. `@Bean CorsConfigurationSource` — CORS 설정 빈, URL 패턴별 등록

## 진행
IDE 열고 `_____` 채워. 끝나면 `/연습 채점`.
