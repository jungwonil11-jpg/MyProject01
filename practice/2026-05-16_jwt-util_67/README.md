# 2026-05-16 JwtUtil — 67% (단일 파일)

## 회차 정보
- **파일**: JwtUtil
- **난이도**: 67%
- **모드**: 단일 파일 (PROGRESS.md 갱신 X)

## 빈칸 카테고리

| 빈칸 자리 | 대략 개수 |
|---|---|
| 필드 타입 (`Key`, `long`) | 3 |
| 메소드 시그니처 (리턴 타입, 파라미터 타입) | ~12 |
| 외부 클래스 참조 (`Jwts`, `Date`, `Claims`, `Keys`, `System`, `SignatureAlgorithm`) | ~14 |
| 외부 메소드 호출명 (`builder`, `setSubject`, `setIssuedAt`, `setExpiration`, `signWith`, `compact`, `parserBuilder`, `setSigningKey`, `build`, `parseClaimsJws`, `getBody`, `getSubject`, `currentTimeMillis`, `getBytes`, `hmacShaKeyFor`) | ~25 |
| `new` 키워드 | ~3 |
| 예외 타입 (`ExpiredJwtException`, `JwtException`, `IllegalArgumentException`, `Exception`) | ~5 |
| 문자열 (`"Token Error"`) | 1 |
| **합계** | **~63** |

## 살린 것 (67% 기준)
- 변수명/필드명 (`secretKey`, `accessToken`, `refreshToken`, `token`, `claims`, `userId`, `e`, `secret`)
- 클래스명 본인 메소드 (`generateAccessToken`, `generateRefreshToken`, `validateAndExtractuserId`, `validateToken`, 생성자명)
- `public`, `private`, `final` 접근/한정자
- 패키지/import 구문
- 주석 (학습 보조)
- `return`, `throw`, `try`, `catch`, `null` (P8)

## JWT 학습 포인트
- **빌더 패턴** — `Jwts.builder().setX().setY()...compact()` 체이닝 구조
- **토큰 생성**: subject(사용자 식별자), issuedAt(발급 시각), expiration(만료 시각), signWith(서명) 4가지 세팅
- **검증 + 파싱**: `parserBuilder().setSigningKey().build().parseClaimsJws(token).getBody()` — 한 번에 서명 검증 + 만료 검증 + payload 파싱
- **두 예외 분기**: `ExpiredJwtException`(만료) vs `JwtException | IllegalArgumentException`(서명 위조 등)

## 진행 방법
1. IDE에서 `JwtUtil.java` 열기
2. `_____` 채우기 (~63개)
3. 끝나면 `/연습 채점`

막히면 `/힌트`.
