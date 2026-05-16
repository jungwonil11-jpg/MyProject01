# 2026-05-16 JwtRequestFilter — 67% (단일 파일)

## 회차 정보
- **파일**: JwtRequestFilter
- **난이도**: 67%
- **모드**: 단일 파일 (PROGRESS.md 갱신 X)

## 빈칸 카테고리

| 빈칸 자리 | 대략 개수 |
|---|---|
| 메소드 시그니처 (리턴 타입, 파라미터 타입 3개, throws 타입 2개) | 6 |
| 로그 메소드 호출 (`log.info`, `log.error`) + 메시지 | ~6 |
| Request/Response/FilterChain 메소드 (`getHeader`, `doFilter`, `setStatus`, `setContentType`, `getWriter`, `write`) | ~15 |
| 문자열 (`"Authorization"`, `"Bearer "`, 콘텐트 타입, JSON 응답) | ~8 |
| 메소드 호출 (`startsWith`, `substring`, `validateToken`) + 인자(`7` 등) | ~5 |
| 클래스 참조 (`UsernamePasswordAuthenticationToken`, `SecurityContextHolder`, `HttpServletResponse`, `List`) | ~6 |
| 외부 메소드 (`List.of()`, `getContext()`, `setAuthentication()`) | ~4 |
| `new` 키워드 | 1 |
| 클래스.필드 (`HttpServletResponse.SC_UNAUTHORIZED` ×3) | 3 |
| 예외 타입 (`ExpiredJwtException`, `Exception`) | 2 |
| **합계** | **~56** |

## 살린 것 (67% 기준)
- 변수명 (`request`, `response`, `filterChain`, `authHeader`, `token`, `userId`, `authenicationToken`, `e`)
- `protected`, `private`, `final` 접근/한정자
- 패키지/import 구문
- 클래스 선언부 + `@Slf4j`, `@Component`, `extends OncePerRequestFilter`
- 필드 선언부 (`@Autowired private JwtUtil jwtUtil;`)
- 주석 (학습 보조)
- `==`, `!=`, `||`, `null`, `return` (P8)

## JWT Filter 학습 포인트
- **OncePerRequestFilter**: 요청당 1번만 실행. 모든 요청이 컨트롤러 도달 전 이 필터 통과
- **3단계 처리**:
  1. Authorization 헤더 추출 + Bearer 검증
  2. JwtUtil로 토큰 유효성 확인
  3. 유효하면 SecurityContextHolder에 인증 정보 등록 → 컨트롤러에서 `@AuthenticationPrincipal` 등으로 꺼내 씀
- **3가지 응답 분기**:
  - 헤더 없음/Bearer X → 통과 (공개 엔드포인트용)
  - 토큰 만료 → 401 "token expired"
  - 토큰 위조/형식 오류 → 401 "token invalid"

## 진행 방법
1. IDE에서 `JwtRequestFilter.java` 열기
2. `_____` 채우기 (~56개)
3. 끝나면 `/연습 채점`

막히면 `/힌트`.
