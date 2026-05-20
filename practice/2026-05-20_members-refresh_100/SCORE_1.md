# 채점 결과 #1 — 2026-05-20 Members 토큰 재발급 100%

## 점수
**231 / 256 (90.2%)**

| 파일 | 점수 |
| --- | --- |
| MembersController.java (refresh) | 196/213 |
| MembersService.java | 3/4 |
| MembersServiceImpl.java | 8/9 |
| MembersMapper.java | 3/4 |
| members-mapper.xml | 21/26 |

---

## MembersController.java (196/213)

### L163: `String storeToken = ...` → `RefreshTokenVO storeToken = ...`
- **왜 틀렸나**: `findRefreshToken()` 리턴 타입이 `RefreshTokenVO`. 토큰 카드 통째 받아야 함. String 으로 받으면 컴파일 에러.
- **외우는 법**: `find~` 류는 무조건 객체(VO) 리턴. login 회차 `findById` 도 같은 패턴 — `MembersVO` 로 받았듯이 여기선 `RefreshTokenVO`.

### L173: `String validateToken = ...` → `String userId = jwtUtil.validateToken(refreshToken)`
- **왜 틀렸나**: 변수명을 `validateToken` 으로 박음. `validateToken()` 메소드는 **검증 후 userId 를 리턴**함. 변수에 들어가는 값은 토큰이 아니라 사용자 ID.
- **외우는 법**: JWT 토큰 안에 payload(userId) 박혀있음. `validateToken()` = 봉투 검증 + 안의 발신자 이름(userId) 추출. 변수명은 의미에 맞춰 `userId`.

### L183: `generateAccessToken(refreshToken)` → `generateAccessToken(userId)`
- **왜 틀렸나**: 새 토큰 발급 시 payload 에 박을 값은 userId. refreshToken 자체를 박으면 토큰 안에 토큰 글자가 박힘 (의미 X).
- **외우는 법**: 4번 단계에서 추출한 userId 가 이후 5~7번 단계에서 계속 재활용됨. **userId 가 refresh 메소드의 중심 변수**.

### L184: `generateRefreshToken(refreshToken)` → `generateRefreshToken(userId)`
- **왜 틀렸나**: 같은 이유.

### L187: `deleteRefreshToken(refreshToken)` → `deleteRefreshToken(userId)`
- **왜 틀렸나**: SQL `delete from refresh_tokens where rt_user_id = #{rt_user_id}` 보면 user_id 기준 삭제. 메소드명이 헷갈리지만 진실은 SQL.
- **외우는 법**: `delete~` 류는 메소드명 신뢰 X. SQL where 조건 보고 결정. 여기선 user_id.

### L189: `setRt_user_id(_____)` → `setRt_user_id(userId)`
- **왜 틀렸나**: 미작성. 새 토큰 저장 시 rt_user_id 필드에 박을 값 = userId.
- **외우는 법**: 필드명 `rt_user_id` 자체가 힌트. user_id 박음.

### L203: `log.info("refresh 사용자 : {}");` → `log.info("refresh 사용자 : {}", userId);`
- **왜 틀렸나**: 메시지에 `{}` 박았는데 변수 인자 미작성. SLF4J 가 `{}` 자리에 끼워 박을 값이 없음.
- **외우는 법**: `{}` 개수 = 변수 인자 개수. {} 박을 거면 변수 박고, 없을 거면 {} 도 안 박음.

### L209: `_____ _____ = _____._____()._____();` → `String userId = e.getClaims().getSubject();` (5자리 미작성)
- **왜 틀렸나**: 통째 미작성. ExpiredJwt 캐치 자리. 토큰 만료된 상황이라 SecurityContext 못 씀 — 만료된 토큰 자체에서 payload 꺼내야 함.
- **외우는 법**: `e.getClaims().getSubject()` 가 만료된 토큰에서 userId 추출하는 표준 패턴. 외워두면 평생 박음.

### L210: `_____._____(_____);` → `membersService.deleteRefreshToken(userId);` (3자리 미작성)
- **왜 틀렸나**: 만료된 토큰이라도 DB에 남은 토큰은 정리해야 함. userId 로 삭제.
- **외우는 법**: 같은 이유.

### L212: `log.info("만료된 사용자 : {}", _____);` → `..., userId);`
- **왜 틀렸나**: 인자 자리 미작성. 위에서 박은 userId 박음.
- **외우는 법**: 같은 이유 (L203).

### L213: `dataVO.setSuccess(Boolean.TRUE)` → `dataVO.setSuccess(Boolean.FALSE)`
- **왜 틀렸나**: 토큰 만료는 **실패 응답**. 클라이언트가 "다시 로그인하세요" 받아야 함. true 보내면 클라이언트가 정상 갱신된 줄 알고 안 됨.
- **외우는 법**: catch 블록 = 실패 처리 자리. 무조건 `Boolean.FALSE`.

---

## MembersService.java (3/4)

### L18: `String findRefreshToken(String refreshToken);` → `RefreshTokenVO findRefreshToken(String refreshToken);`
- **왜 틀렸나**: 리턴 타입 String 박음. 정답은 RefreshTokenVO. find~ 류는 객체 리턴.
- **외우는 법**: 같은 이유 (Controller L163).

---

## MembersServiceImpl.java (8/9)

### L35: `public String findRefreshToken(...)` → `public RefreshTokenVO findRefreshToken(...)`
- **왜 틀렸나**: 동일. 인터페이스와 임플 시그니처 일치해야 함.
- **외우는 법**: 같은 이유.

---

## MembersMapper.java (3/4)

### L19: `String findRefreshToken(String refreshToken);` → `RefreshTokenVO findRefreshToken(String refreshToken);`
- **왜 틀렸나**: 동일.
- **외우는 법**: 5-파일 일관성. Service ↔ Impl ↔ Mapper 시그니처 다 동일해야 함.

---

## members-mapper.xml (21/26)

### L17: `delete from rt_token where ...` → `delete from refresh_tokens where ...`
- **왜 틀렸나**: 테이블명 잘못 박음. `rt_token` 은 컬럼명, `refresh_tokens` 가 테이블명. 컬럼이랑 테이블 헷갈림.
- **외우는 법**: 테이블명 = 복수형 (`refresh_tokens`, `members`). 컬럼은 prefix (`rt_token`, `m_id`). 헷갈리면 DB 스키마 확인.

### L21: `insert into rt_token ...` → `insert into refresh_tokens ...`
- **왜 틀렸나**: 같은 이유.

### L24: `resultType="String"` → `resultType="RefreshTokenVO"`
- **왜 틀렸나**: select 결과 타입 String 으로 박음. 정답은 RefreshTokenVO. `select *` 라면 row 통째 매핑되어 객체 리턴.
- **외우는 법**: `select *` → 객체 리턴. `select 컬럼1` → 그 컬럼 타입 리턴. 결과 컬럼 개수로 판단.

### L25: `select rt_token from refresh_tokens rt_token=#{rt_token}` → `select * from refresh_tokens where rt_token=#{rt_token}`
- **왜 틀렸나 (1)**: `select rt_token` → `select *` 박아야 함. 객체 매핑하려면 모든 컬럼 받아야 함.
- **왜 틀렸나 (2)**: **`where` 키워드 누락**. SQL 조건절 시작은 무조건 `where`. login 회차 SCORE 에도 `when` 으로 오답 있었음 — 같은 실수 반복.
- **외우는 법**: SQL 조건절 = `where` 무조건. SELECT 패턴 = `select * from 테이블 where 컬럼=#{변수}`. 통으로 외워두면 평생 안 헷갈림.
