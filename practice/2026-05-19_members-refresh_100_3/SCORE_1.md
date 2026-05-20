# 채점 결과 #1 — 2026-05-19 Members 토큰 재발급 100% (3회차)

> 이전 회차:
> - SCORE_1 (1회차): 155/256 (60.5%) — 5파일
> - SCORE_1 (2회차): 142/256 (55.5%) — controller만
> - **이번 (3회차): 245/256 (95.7%)** — 5파일 풀이, 큰 도약

## 점수
**245 / 256 (95.7%)** ⭐

| 파일 | 점수 |
|---|---|
| MembersController.java (refresh) | 161/171 |
| MembersService.java | 12/12 |
| MembersServiceImpl.java | 28/28 |
| MembersMapper.java | 11/12 |
| members-mapper.xml | 33/33 |

> 채점 룰 적용:
> - isBlank ↔ isEmpty 둘 다 정답 (CLAUDE.md)
> - Boolean.TRUE ↔ true 의미 동일
> - 데이터성 문자열 의미 동일 OK
> - 5-파일 묶음 메소드명/리턴타입 5곳 일관성 채점

---

## MembersController.java (161/171) — 오답 4곳

### 🔴 미작성 (만료 catch 본문 — 1·2회차 동일 실수)

**L188: `_____ _____ = _____._____()._____();`** → `String userId = e.getClaims().getSubject();` (5자리 빈칸 그대로 = 5점 손실)
- ExpiredJwt catch 의 핵심 흐름. 만료 토큰에서 userId 추출.
- `e.getClaims()` = 만료된 토큰의 payload (서명은 무효지만 claims 자체는 읽힘). `.getSubject()` = 토큰 발급 시 박은 sub 필드 (= userId).
- **외우는 법**: 만료 catch = "이 토큰 누구 거야? → DB 에서 그 사람 토큰 지우기". 8글자 흐름 `e.getClaims().getSubject()` 통째 외움.

**L189: `_____._____(_____);`** → `membersService.deleteRefreshToken(userId);` (3자리 빈칸 그대로 = 3점 손실)
- 만료된 토큰은 DB 에서 즉시 제거. 클라이언트는 재로그인 유도.
- **외우는 법**: L188 에서 userId 빼면 → 무조건 다음 줄 deleteRefreshToken. 두 줄 세트.

### 🔴 log SLF4J 두 번째 인자 미작성

**L191: `log.info("만료", _____);`** → `log.info("만료: userId={}", userId);` (1자리 빈칸 그대로 = 1점 손실)
**L196: `log.error("예측 못 한 예외", _____);`** → `log.error("예측 못 한 예외", e);` (1자리 빈칸 그대로 = 1점 손실)
- L191 두 번째 인자: `userId` (L188 에서 추출한 거)
- L196 두 번째 인자: `e` (예외 객체 — 스택트레이스 보존)
- **외우는 법**: catch 블록 안 log = 두 번째 인자에 무조건 뭐 박힘 (예외 객체 또는 userId 같은 컨텍스트).

---

## MembersMapper.java (11/12) — 오답 1곳

### 🔴 리턴 타입 (자기 코드 일관성 깨짐)

**L15: `String deleteRefreshToken(String userId);`** → `void deleteRefreshToken(String userId);`
- delete 는 반환할 게 없음 → `void` 가 맞음.
- 사용자 본인 코드 일관성 체크:
  - Service L12: `void deleteRefreshToken(String userId);` (void)
  - ServiceImpl L25: `public void deleteRefreshToken(String userId)` (void)
  - Mapper L15: **`String` deleteRefreshToken(...)** ❌ — Mapper 만 String
  - XML L11: `<delete id="deleteRefreshToken">` (delete 태그 = void 와 자연 매칭)
- → 4곳은 void, Mapper 만 String → **5-파일 일관성 깨짐**
- **외우는 법**: SQL 동작별 매퍼 리턴 타입 패턴
  - `<select>` → 리턴 타입 박힘 (단일 VO 또는 List)
  - `<insert>` / `<update>` / `<delete>` → 보통 `void` (또는 `int` for affected rows)
  - 헷갈리면 XML 태그 보고 결정. delete = void 디폴트.

---

## 🟡 학습 포인트 (점수 차감 X — 채점은 통과)

### 1. `findRefreshToken` 리턴 타입 선택 (String vs RefreshTokenVO)

사용자는 5곳에서 `String` 으로 일관 → 채점 ✅. 강사는 `RefreshTokenVO` 통일.

| 위치 | 사용자 (String) | 강사 (RefreshTokenVO) |
|---|---|---|
| Controller L141 | `String storeToken = ...` | `RefreshTokenVO storeToken = ...` |
| Service L16 | `String findRefreshToken(...)` | `RefreshTokenVO findRefreshToken(...)` |
| ServiceImpl L35 | `public String findRefreshToken(...)` | `public RefreshTokenVO findRefreshToken(...)` |
| Mapper L19 | `String findRefreshToken(...)` | `RefreshTokenVO findRefreshToken(...)` |
| XML L19-20 | `resultType="String"` + `select rt_token` | `resultType="RefreshTokenVO"` + `select *` |

**둘 다 동작 OK**. 차이:
- 사용자 방식 (String): rt_token 컬럼만 SELECT → 토큰 존재 여부만 체크. 가볍고 깔끔.
- 강사 방식 (RefreshTokenVO): 행 전체 SELECT → 추후 rt_user_id 등 다른 필드 활용 가능. 확장성.

용도가 "토큰 존재 확인" 뿐이면 사용자 방식이 더 효율적. 학습 가치 있음 ㅇㅈ.

### 2. log SLF4J `{}` placeholder 빠짐

**L182: `log.info("갱신한 사용자 : ", userId);`** — 채점 ✅ (의미 동일), 하지만 실제 동작 다름
- SLF4J 패턴: `log.info("메시지 {}", 변수)` → `{}` 자리에 변수 박혀서 출력
- 사용자 코드: `{}` 없으면 두 번째 인자 무시됨 → 실제 로그에 userId 안 찍힘
- **고치는 법**: `log.info("갱신한 사용자 : {}", userId);` — `{}` 한 글자 추가만으로 의도대로 동작
- 채점 룰 상 데이터성 문자열은 의미 동일 OK 라 ✅ 처리. 다만 실무에선 `{}` 빠지면 디버깅 X.

### 3. `isEmpty` 사용 (CLAUDE.md 룰로 ✅)

**L132: `refreshToken.isEmpty()`** — 채점 ✅ (isBlank 와 동등 처리 — CLAUDE.md 룰)
- 본인이 isBlank 통일하기로 했으니 다음부턴 `isBlank` 박는 습관.
- 동작 차이: `"   "` (공백 3칸) → isEmpty 못 잡음, isBlank 잡음.

---

## 1·2회차 대비 도약 (155 → 142 → 245)

| 항목 | 1회차 | 2회차 | 3회차 |
|---|---|---|---|
| 메서드 시그니처 (L124-125) | ❌ | ❌ | ✅ |
| L129 변수 타입/추출 | ❌ | ❌ | ✅ |
| L132 빈값 체크 | ❌ | ❌ | ✅ (isEmpty/isBlank 동등) |
| L141 DB 조회 | ❌ | ❌ | ✅ (5곳 일관) |
| L152 JWT 검증 (validateToken) | ❌ | ❌ | ✅ |
| 로테이션 6단계 (saveRefreshToken) | ❌ | ❌ | ✅ |
| setSuccess 분기 4곳 | ❌ | ❌ | ✅ |
| L157 setMessage 오타 | ❌ | ❌ | ✅ |
| 만료 catch 본문 (L188-189) | ❌ | ❌ | ❌ |
| log 두 번째 인자 (L191/L196) | ❌ | ❌ | ❌ |

**큰 흐름 다 익힘**. 남은 건 만료 catch 본문 + log 두 번째 인자 — 둘 다 catch 블록 안쪽 자잘한 자리.

---

## 다음 회차 전략 (4회차 목표: 만점 256/256)

만점까지 11점 차이. 모든 오답이 **catch 블록 안쪽** 에 몰림 → 그 부분만 외우면 끝.

### 외울 패턴 1: 만료 catch 8자리 (L188-189) — 9점 손실 자리
```java
} catch (ExpiredJwtException e) {
    String userId = e.getClaims().getSubject();   // L188 — 5자리
    membersService.deleteRefreshToken(userId);     // L189 — 3자리
    log.info("[LEARN] 만료 : userId={}", userId); // L191 — 두 번째 인자 userId
    dataVO.setSuccess(Boolean.FALSE);
    dataVO.setMessage("...");
}
```

### 외울 패턴 2: 예외 catch log 인자 (L196) — 1점 손실 자리
```java
} catch (Exception e) {
    log.error("메시지", e);  // ← 두 번째 인자 무조건 e (스택트레이스 보존)
    ...
}
```

### 외울 패턴 3: Mapper delete 리턴 타입 (L15) — 1점 손실 자리
- `<delete>` 매핑 = 무조건 `void` 리턴
- 4곳 (Service/Impl/XML 태그) 다 void 인데 Mapper 만 String 박지 말 것

이 3 패턴 외우면 만점.
