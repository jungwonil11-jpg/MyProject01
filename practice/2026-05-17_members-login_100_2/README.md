# 2026-05-17 Members 로그인 100% (2회차)

> 이전 회차: `2026-05-17_members-login_100/SCORE_1.md` (90/192, 47.0%)

## 회차 정보
- 시작: 2026-05-17
- 도메인: Members
- 기능: 로그인 (`POST /members/login`)
- 난이도: **100%**
- 템플릿: `practice/_templates/members-login_100/` 에서 자동 복사

## 학습 대상 파일

1. `src/main/java/com/study/myproject01/members/controller/MembersController.java` (`getLogin` 메소드)
2. `src/main/java/com/study/myproject01/members/service/MembersService.java`
3. `src/main/java/com/study/myproject01/members/service/MembersServiceImpl.java`
4. `src/main/java/com/study/myproject01/members/mapper/MembersMapper.java`
5. `src/main/resources/mapper/members-mapper.xml`

## 이번 회차 목표 — 1회차 오답 잡기

### 핵심 패턴 (꼭 외울 것)

1. **`@PostMapping("/login")`** — 로그인은 POST (URL에 비밀번호 노출 X)
2. **`@RequestBody MembersVO mvo`** — JSON 두 필드(m_id, m_pw) 받으려면 VO 객체로
3. **`MembersVO membersVO = ...`** — 파라미터 mvo와 **다른 이름**으로 새 변수 (변수명 충돌 방지)
4. **`mvo.getM_id()`** — DB 컬럼 `m_id` → getter `getM_id` (m_idx 같은 거 없음)
5. **`Boolean.FALSE`** — `if(실패조건)` 블록은 FALSE / `catch` 블록도 FALSE / 정상 종료만 TRUE
6. **`return dataVO;`** — 실패 case 안에서 일찍 종료 박기. 2번 (mvo null + 비밀번호 틀림)

### 토큰 로테이션 패턴 (1회차 미작성)

```java
// 1. 기존 refreshToken 삭제
membersService.deleteRefreshToken(membersVO.getM_id());

// 2. 새 VO 만들기 + setter
RefreshTokenVO refreshTokenVO = new RefreshTokenVO();
refreshTokenVO.setRt_user_id(membersVO.getM_id());
refreshTokenVO.setRt_token(refreshToken);

// 3. DB 저장
membersService.saveRefreshToken(refreshTokenVO);
```

### Map 응답 조립 패턴 (1회차 미작성)

```java
Map<String, Object> map = new HashMap<>();
map.put("accessToken", accessToken);
map.put("refreshToken", refreshToken);
map.put("membersVO", membersVO);

dataVO.setSuccess(true);
dataVO.setMessage("로그인 성공");
dataVO.setData(map);     // ← mvo 아니라 map
```

### 5-파일 묶음 3개 메소드 일관성

`findById`, `deleteRefreshToken`, `saveRefreshToken` 세 메소드를 **5곳에 동일 이름**으로 박을 것:
- Controller 호출
- Service 시그니처
- ServiceImpl `@Override`
- Mapper 시그니처
- XML `id` 속성

## 진행 방법

1. IntelliJ에서 5개 파일 열기
2. `_____` 채우기
3. 컴파일 에러 확인 (변수명 충돌 주의)
4. 끝나면 `/연습 채점`

## 1회차 SCORE.md 참고

`../2026-05-17_members-login_100/SCORE_1.md` 에 1회차 오답 상세 + 외우는 법 박혀있음. 풀기 전에 한 번 훑고 가면 도움.
