# 2026-05-19 Members 토큰 재발급 100% (3회차)

## 회차 정보
- 기능: 토큰 재발급 (refresh)
- 난이도: 100% ([LEARN] 로그 자리 6개 포함)
- 호출: `/도전100 2`
- 시작: 2026-05-19
- 이전 회차:
  - `2026-05-19_members-refresh_100/` (SCORE_1: 155/256, 60.5%) — 5파일 풀이
  - `2026-05-19_members-refresh_100_2/` (SCORE_1: 142/256, 55.5%) — controller만 풀이

## 학습 대상 파일
1. `src/.../controller/MembersController.java` — getRefreshToken 빈칸
2. `src/.../service/MembersService.java` — 3개 시그니처 빈칸 (findRefreshToken, deleteRefreshToken, saveRefreshToken)
3. `src/.../service/MembersServiceImpl.java` — 3개 @Override 본문 빈칸
4. `src/.../mapper/MembersMapper.java` — 3개 시그니처 빈칸
5. `src/main/resources/mapper/members-mapper.xml` — 3개 SQL 빈칸

## 진행 방법
IntelliJ에서 5개 파일 열고 `_____` 채우기. 끝나면 `/연습 채점`.

## 2회차 복습 포인트 (자주 틀린 자리)

### 메서드 시그니처 (점수 영향 X, 일관성)
- 메서드명: `getRefreshToken` (Token 까지 박아야 다른 메서드와 일관)
- Map 제네릭: `Map<String, String>` (Object 아님)

### 흐름 7단계 핵심
1. `String refreshToken = body.get("refreshToken");` — 변수 타입 **String**
2. `if(refreshToken == null || refreshToken.isBlank())` — body 자체 X, **추출한 토큰** 체크
3. `membersService.findRefreshToken(refreshToken)` — DB 조회 (메서드명 5곳 일관)
4. `String userId = jwtUtil.validateToken(refreshToken);` — JWT 검증, **findById 아님**
5. 새 토큰 2개 생성 (`newAccessToken`, `newRefreshToken` — `new` 접두사로 구분)
6. **`membersService.saveRefreshToken(newToken)`** — DB 저장 (setData 박으면 X)
7. map 만들고 setSuccess + setMessage + setData

### setSuccess 분기 (4곳 헷갈리던 거)
- 실패 분기 (L145/L156/L192/L197): 무조건 `Boolean.FALSE`
- 성공 분기 (L177): `Boolean.TRUE`
- 외우는 법: setSuccess = "이 요청 성공했나?" → 실패 = FALSE

### 만료 catch 본문 (8자리)
```java
String userId = e.getClaims().getSubject();
membersService.deleteRefreshToken(userId);
```

### log SLF4J 두 번째 인자 (3곳)
- L182: refresh 성공 → `userId`
- L191: 만료 → `userId`
- L196: 예외 → `e` (스택트레이스 보존)

### 작은 오타 주의
- L157: `setMessage` (`get` 박지 X)
