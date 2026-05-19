# 채점 결과 #1 — 2026-05-19 Members 토큰 재발급 100% (2회차, controller만 풀이)

> 1회차: SCORE_1 (155/256, 60.5%) — 5파일 풀이
> 이번: controller 만 풀이 → 다른 4파일 미작성 (의도된 부분 학습)

## 점수
**142 / 256 (55.5%)** — 5파일 전체 기준
**142 / 171 (83.0%)** — controller refresh 부분 기준 ⭐

| 파일 | 점수 |
|---|---|
| MembersController.java (refresh) | 142/171 |
| MembersService.java | 0/12 (미작성) |
| MembersServiceImpl.java | 0/28 (미작성) |
| MembersMapper.java | 0/12 (미작성) |
| members-mapper.xml | 0/33 (미작성) |

> 채점 룰: 데이터성 문자열 = 의미 동일 ✅, 값 토큰 (Boolean.TRUE/FALSE ↔ true/false) = 의미 동일 ✅

---

## MembersController.java (142/171) — 오답 전부

### 🟡 메서드 시그니처 (L124-125) — 채점 ✅, 학습 포인트로만

**L124: `public DataVO getRefresh(@RequestBody Map<String, Object> body)`** vs 강사 `public DataVO getRefreshToken(@RequestBody Map<String, String> body)`
- **둘 다 동작 OK** — 점수 차감 X.
  - 메서드명: 클래스 내부 식별자. URL 매핑은 `@PostMapping("/refresh")` 가 함. 메서드명 뭐든 외부 API 동작 영향 0.
  - Map 제네릭: `Object` 도 컴파일 통과 + 동작 OK (캐스팅 필요할 뿐). 사용자가 L129 에서 RefreshTokenVO 로 박은 게 ❌ 인 거지 제네릭 자체는 안 틀림.
- **강사 코드 의도** (학습 가치):
  - 메서드명: `getLogin`/`getMyPage`/`getLogout` 패턴 따라 `getRefreshToken` — 일관성.
  - 제네릭 `String`: body 가 `{"refreshToken": "..."}` 단일 키-단일 문자열이라 `String` 박으면 L129 에서 캐스팅 없이 `String 토큰 = body.get(...)` 깔끔. login 처럼 VO 통째로 받을 일 X (login 은 `@RequestBody MembersVO mvo` 따로).
- **다음 회차**: 점수 영향은 없어도 강사 코드 따라가는 게 일관성 + 흐름 자연스러움.

### 🔴 흐름 오해 (1회차와 같은 실수)

**L129: `RefreshTokenVO refreshTokenVO = body.get("refreshToken");`** → `String refreshToken = body.get("refreshToken");`
- body.get 의 리턴 타입은 Map 의 V 타입 = String (시그니처 고치고 나면). RefreshTokenVO 아님.
- 변수 타입 = String 박아야 함. RefreshTokenVO 는 DB 행 매핑용 (rt_user_id, rt_token 두 필드). 클라이언트가 보내는 raw 토큰 문자열을 담을 자리는 String.

**L132: `if(body == null || body.isEmpty())`** → `if(refreshToken == null || refreshToken.isBlank())`
- body 자체 체크가 아니라 **추출한 토큰** 체크. body 가 비었으면 Spring 이 400 던지지 여기까지 안 옴.
- isEmpty 는 Map/Collection 용, String 은 isBlank (공백만 있어도 false 반환 — 더 안전).

**L141: `membersService.findByRefreshToken(rt_user_id)`** → `membersService.findRefreshToken(refreshToken)`
- 메서드명 `findByRefreshToken` (사용자) vs `findRefreshToken` (강사) — 5-파일 일관성 룰 적용되려면 5곳 다 박혀야 OK. 이번엔 controller 만 풀어서 일관성 X → ❌.
- 인자 `rt_user_id` — 정의 안 된 변수 (컴파일 에러). 위 L129 의 refreshTokenVO 도 안 쓰고 → 결국 L129 변수 무용지물.

**L152: `RefreshTokenVO userId = membersService.findById(id);`** → `String userId = jwtUtil.validateToken(refreshToken);`
- 1회차와 똑같은 실수 — 4단계는 JWT 검증 (`jwtUtil.validateToken`), findById 아님.
- 변수 타입 RefreshTokenVO ❌ (String 이 정답 — validateToken 의 리턴이 subject = userId 문자열), 인자 `id` 미정의 (컴파일 에러).
- **외우는 법**: 3단계(DB) 통과해도 4단계(JWT 서명 검증) 따로 함. DB 비교는 "이 토큰 우리가 발급한 거 맞아?", JWT 검증은 "이 토큰 위조 안 됐어?". 둘 다 통과해야 안전.

### 🔴 Boolean.TRUE/FALSE 분기 헷갈림 (4곳)

**L145, L156, L192, L197**: 모두 **실패 분기에 `Boolean.TRUE` 박음** → `Boolean.FALSE` 가 정답.
- 실패 응답 (DB 없는 토큰 / JWT 검증 실패 / 만료 / 일반 예외) 인데 success=TRUE 박으면 클라이언트가 성공으로 인식.
- **외우는 법**: setSuccess = "이 요청 성공했나?" 의 답. 실패 분기엔 무조건 FALSE.
- **자동화 패턴**: try 안 정상 라인 = TRUE, 모든 `if (실패조건)` 안 + 모든 catch = FALSE. 예외 없음.

### 🔴 호출 자체가 잘못 (DB 저장 누락 — 본질)

**L170: `dataVO.setData(newRefreshToken);`** → `membersService.saveRefreshToken(newToken);`
- 6단계 = 새 refreshToken **DB 저장**. setData 는 응답 만들기.
- 이 한 줄 잘못 박아서 → 새 토큰이 DB 에 저장 X → 다음 refresh 요청 때 L141 (`findRefreshToken`) 에서 못 찾고 무효 토큰 처리. **로테이션 실패 = 토큰 시스템 깨짐**.
- 게다가 L179 에서 또 `setData(map)` 박으니까 응답 setData 는 마지막 거(map)만 살아남음 → 응답엔 영향 적지만 DB 저장 누락이 진짜 버그.
- **외우는 법**: 6단계 키워드 "기존 삭제 → 새 객체 만들기 → **DB 저장**". setData 는 7단계 응답 만들 때 단 한 번만.

### 🔴 작은 오답

**L157: `dataVO.getMessage("검증 실패")`** → `dataVO.setMessage(...)`
- 오타 — `set` 박을 자리에 `get` 박음. 컴파일 에러 (getMessage 는 인자 없는 getter).

### 🟡 변수명 학습 포인트 (채점 ✅ — 사용자 코드 내 일관성 OK, 학습용으로만)

**L162-163, L167-169**: 강사는 `newAccessToken`, `newRefreshToken`, `newToken` 으로 `new` 접두사 박음. 사용자는 `accessToken`, `refreshToken`, `newRefreshToken`.
- 채점은 통과 — 사용자 코드 내에서 변수명 일관 + map.put 까지 같이 박힘.
- 학습 포인트: **`new` 접두사 박는 이유** — 같은 메서드에 "오래된 토큰" (L129 body 에서 추출한 거) 과 "새로 만든 토큰" (L162-163) 이 공존함. prefix 없으면 둘이 헷갈림. 강사 코드 의도는 명시적 구분.
- 사용자는 L129 에서 `refreshTokenVO` (틀린 타입), L163 에서 `refreshToken` 박았는데, 만약 L129 를 정답인 `String refreshToken` 으로 박았다면 L163 에서 변수명 충돌남. 그래서 강사가 L163 을 `newRefreshToken` 으로 박은 거.
- **다음 회차**: `new` 접두사 의식적으로 박아서 헷갈림 차단.

### 🟢 미작성 (만료 catch 본문)

**L188-189**: `String userId = e.getClaims().getSubject();` + `membersService.deleteRefreshToken(userId);` 통째 미작성 (8자리 0점).
- ExpiredJwt catch 의 핵심 흐름. 만료 토큰에서 userId 추출 → DB 삭제.
- `e.getClaims()` = 만료된 토큰의 payload (서명은 무효지만 claims 자체는 읽을 수 있음). `.getSubject()` = 토큰 발급 시 박은 sub 필드 (= userId).
- **외우는 법**: 만료 catch = "이 토큰 누구 거? → DB 에서 그 사람 토큰 지우기". `e` 는 ExpiredJwtException, claims 가 노출돼 있음.

**L182, L191, L196**: `log.info/error(메시지, 인자)` 의 **두 번째 인자 미작성** (3곳).
- L182: refresh 성공 로그 → 두 번째 인자 `userId` (`{}` 자리에 박힘)
- L191: 만료 로그 → 두 번째 인자 `userId` (L188 에서 추출한 거)
- L196: 일반 예외 로그 → 두 번째 인자 `e` (스택트레이스 박혀야 디버깅 가능)
- **외우는 법**: SLF4J 패턴 = `log.레벨("메시지 {}", 변수)`. `{}` 개수 = 변수 개수. 예외는 마지막 인자에 객체 자체 (`{}` 없이도 스택트레이스 출력됨).

---

## 1회차 대비 개선 (142 vs 88)

- 응답 흐름 (map.put + setSuccess + setMessage + setData) 다 박음 → +20점
- 새 토큰 생성 + 로테이션 잘 박음 → +10점
- 데이터성 문자열 의미만 맞추면 됨을 인지하고 박음
- 일부 헷갈리던 catch 두 개 정확히 박음

## 여전히 어려운 부분

- 메서드 시그니처 (메서드명 + Map 제네릭) — 흐름 풀기 전에 박는 자리라 매번 빠짐
- 흐름 4단계 (JWT 검증) — `jwtUtil.validateToken` 아직 박힘 X (1회차와 동일 실수)
- Boolean.TRUE/FALSE 분기 — 실패 분기에 TRUE 박는 습관
- L132 if 빈값 체크 — body 자체 체크 vs 토큰 체크 헷갈림
- L170 saveRefreshToken — setData 와 위치 헷갈림
- 만료 catch 본문 흐름

---

## 다음 회차 전략

1. **시그니처부터 박음**: `public DataVO getRefreshToken(@RequestBody Map<String, String> body)` — 5자 prefix `getRef` 까지 박고 `reshToken` 까지 박는 습관
2. **L129/L132 흐름 외움**: `String 토큰 = body.get("키")` → `if(토큰 == null || 토큰.isBlank())`
3. **L152 JWT 검증**: `String userId = jwtUtil.validateToken(refreshToken);` — `findById` 아님, 무조건 `validateToken`
4. **setSuccess 분기 자동화**: try 안 성공 라인 = TRUE, 모든 if 실패 + catch = FALSE
5. **6단계 = DB 저장**: 새 객체 만들고 → `saveRefreshToken(newToken)` — `setData` 박으면 안 됨
6. **만료 catch 본문 외움**: `e.getClaims().getSubject()` → `deleteRefreshToken(userId)` → log + setSuccess(FALSE) + setMessage
7. **변수 prefix `new`**: 새로 만든 거에 `new` 박아서 기존 거랑 구분
8. **다음엔 5파일 다 풀어서 5-파일 일관성 확인** — findByRefreshToken 박을 거면 5곳 다 같이
