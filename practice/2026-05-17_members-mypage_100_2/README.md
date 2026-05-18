# 2026-05-17 Members 마이페이지 100% (2회차)

## 회차 정보
- 시작: 2026-05-17 (재도전)
- 도메인: Members
- 기능: 마이페이지 (`GET /members/myPage`)
- 난이도: **100%** — 빈칸 풀 전부
- 이전 회차:
  - `2026-05-17_members-mypage_33` (10/14, 71%)
  - `2026-05-17_members-mypage_67` (27/27, 100% ✅)
  - `2026-05-17_members-mypage_100` (59/68, 87% — 이번에 다시)

## 학습 대상 파일 (src/ 직접 작업)
1. `src/main/java/com/study/myproject01/members/controller/MembersController.java` — `getMyPage` 메소드
2. `src/main/java/com/study/myproject01/members/service/MembersService.java` — `findById` 시그니처
3. `src/main/java/com/study/myproject01/members/service/MembersServiceImpl.java` — `findById` 구현
4. `src/main/java/com/study/myproject01/members/mapper/MembersMapper.java` — `findById` 시그니처
5. `src/main/resources/mapper/members-mapper.xml` — `<select id="...">` 1자리

## 이번 회차 포커스
이전 87%에서 틀린 4가지 뿌리 다시 짚을 것:
1. **`findById` 카멜케이스 5곳 일관성** — Controller·Service·ServiceImpl·Mapper·XML 한 글자도 안 다르게
2. **컨트롤러 메소드명**은 URL 의도를 따름 (`/myPage` → `getMyPage`)
3. **DB 조회 결과 null 체크** — `service.findXxx()` 직후 변수가 null이냐 봄. `userId`는 토큰에서 꺼낸 거라 null 아님
4. **응답 데이터는 조회된 객체 통째** (`setData(mvo)`), 토큰 ID 문자열(`setData(userId)`)이 아님

## 변수명 짓기 (의미상 정확)
- DB 조회 결과 받는 변수: `mvo` / `member` / `memberInfo` — 객체 전체를 담는 단수 명사
- `memberId`는 ❌ ("ID"는 문자열 1개 뉘앙스, 객체랑 안 맞음)

## 진행 방법
1. IntelliJ에서 5개 파일 열고 `_____` 채움
2. 다른 기능 메소드(로그인·토큰재발급)는 손대지 말 것 — `findById` 시그니처 채우면 자동 컴파일됨
3. 끝나면 `/연습 채점`

## 5-파일 일관성 체크리스트 (작성 후 한 번 더 봄)
- [ ] Controller `membersService._____` ↔ Service `_____` ↔ ServiceImpl `@Override _____` + `membersMapper._____` ↔ Mapper `_____` ↔ XML `id="_____"` — 5곳 한 글자도 안 다름?
- [ ] 컨트롤러 메소드명이 URL과 매칭됨?
- [ ] `if(_____ == null)` 변수가 DB 조회 결과 변수임?
- [ ] `setData(_____)` 인자가 DB 조회 결과 객체 변수임?
