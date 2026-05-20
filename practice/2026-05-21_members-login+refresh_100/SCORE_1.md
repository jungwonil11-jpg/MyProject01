# 채점 결과 #1 — 2026-05-21 Members 로그인+토큰재발급 100%

## 점수
**303 / 310 (97.7%)**

| 기능 | 점수 |
| --- | --- |
| 로그인 | ~108/110 |
| 토큰 재발급 | ~195/200 |

| 파일 | 점수 |
| --- | --- |
| MembersController.java (login + refresh) | 257/260 |
| MembersService.java | 15/16 |
| MembersServiceImpl.java | 34/36 |
| MembersMapper.java | 15/16 |
| members-mapper.xml | 52/52 |

---

## MembersController.java (257/260)

### L108: `log.error("예외 발생  : {}", e)` → `log.error("예외 발생: m_id={}", mvo.getM_id(), e)`
- **왜 틀렸나**: 메시지에 `{}` 1개 박았는데 변수 인자 자리에 `e` 만 박음. 정답은 인자 자리에 **`mvo.getM_id()` (변수) + `e` (예외 객체)** 두 개 박혀야 함. 2자리 미작성.
- **외우는 법**: `log.error` 패턴 = `("메시지: {}", 변수, e)`. **변수 자리 + `e` 자리는 별개**. {} 박았으면 변수 박고, error 자리엔 끝에 무조건 `e`.

### L214: `dataVO.setSuccess(Boolean.TRUE)` → `dataVO.setSuccess(Boolean.FALSE)`
- **왜 틀렸나**: ExpiredJwtException catch 자리 = **토큰 만료 = 실패 응답**. 클라이언트가 "다시 로그인" 받아야 함. TRUE 박으면 클라이언트가 정상 갱신된 줄 알고 동작 X.
- **외우는 법**: **catch 블록 = 실패 처리 = 무조건 `Boolean.FALSE`**. login 회차에서도 같은 실수 — 두 번째 반복임.

---

## MembersService.java (15/16)

### L16: `RefreshTokenVO saveRefreshToken(...)` → `void saveRefreshToken(...)`
- **왜 틀렸나**: 리턴 타입 `void` 박아야 하는데 `RefreshTokenVO` 박음. `saveRefreshToken` 은 DB insert 만 하고 결과 안 줘도 됨 → void.
- **외우는 법**: **`save~`/`delete~`/`register` 류는 void**. 객체 박을 거 없음. find~ 만 객체 리턴.

---

## MembersServiceImpl.java (34/36)

### L30: `public RefreshTokenVO saveRefreshToken(...) { return ... }` → `public void saveRefreshToken(...) { ... }`
- **왜 틀렸나**: 같은 이유. 인터페이스 시그니처와 일치해야 함. `void` 인데 `RefreshTokenVO` 박고 `return` 까지 박음. **2자리 틀림** (리턴 타입 + return 키워드).
- **외우는 법**: 인터페이스 ↔ 임플 시그니처 반드시 일치. void 면 return X.

---

## MembersMapper.java (15/16)

### L17: `RefreshTokenVO saveRefreshToken(...)` → `void saveRefreshToken(...)`
- **왜 틀렸나**: 같은 이유. 5-파일 일관성 깨짐.
- **외우는 법**: Service ↔ Impl ↔ Mapper 시그니처 다 동일. 한 곳 틀리면 3곳 다 틀림 (3 → 1 회 실수가 점수에 3배 영향).

---

## members-mapper.xml (52/52)

전부 정답 ✅. SQL 다 잘 박았음.

---

## 평가 (전체)

- **변수명 `userId` 일관성** 잘 박음 (Service/Impl/Mapper 다 통일). 정답 `id` 와 다르지만 사용자 일관성 룰로 ✅
- **JWT 패턴 (`e.getClaims().getSubject()`)** 외워서 박음 — 이전 회차 학습 효과 ↑
- **`saveRefreshToken` 리턴 타입** 3곳 일관성 깨짐 — 한 곳 실수가 3곳에 전파. 다음엔 시그니처 박을 때 한 번 더 확인.
- **catch 블록 setSuccess 값** — login/refresh 둘 다 패턴 동일하게 외워두기.
