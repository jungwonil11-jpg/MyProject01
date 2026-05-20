# 채점 결과 #1 — 2026-05-20 Members 로그인 100%

## 점수
**62 / 72 (86.1%)**

| 파일 | 점수 |
| --- | --- |
| MembersController.java (login) | 33/42 |
| MembersService.java | 4/4 |
| MembersServiceImpl.java | 9/9 |
| MembersMapper.java | 4/4 |
| members-mapper.xml | 12/13 |

---

## MembersController.java (33/42)

### L51: `log.info("로그인시도", _____._____())` → `log.info("로그인 시도", mvo.getM_id())`
- **왜 틀렸나**: 두 번째 인자 자리 미작성. `_____._____()` 그대로 둠. 첫 번째 인자(메시지)는 의미 동일이라 ✅ 처리.
- **외우는 법**: `log.info("메시지: {}", 값)` 의 `{}` 자리에 들어갈 값은 항상 mvo.getM_id() 같은 변수. **로그는 메시지 + 변수 2개 한 쌍**.

### L58: `log.warn("는 아이디로 시도", _____._____())` → `..., mvo.getM_id())`
- **왜 틀렸나**: 같은 이유 — 두 번째 인자 미작성.
- **외우는 법**: 같은 이유.

### L66: `log.warn("비번 틀림", _____._____())` → `..., mvo.getM_id())`
- **왜 틀렸나**: 같은 이유.

### L89: `map.put("_____",_____)` → `map.put("membersVO", membersVO)`
- **왜 틀렸나**: 두 자리 다 미작성. 클라이언트 응답 패키지에 회원 정보(membersVO) 박는 자리.
- **외우는 법**: 위 map.put 두 줄(`accessToken`, `refreshToken`)과 동일 패턴. key=변수명, value=변수. 세 번째는 회원 정보 통째 = `("membersVO", membersVO)`.

### L93: `dataVO.setMessage("토큰 갱신")` → `dataVO.setMessage("로그인 성공")`
- **왜 틀렸나**: 데이터성 문자열 자리인데 의미가 다름. "토큰 갱신" = refresh API 응답 메시지. login 흐름 마지막 자리에 박힐 거는 **"로그인 성공"**.
- **외우는 법**: 함수명 = 메시지 단서. `getLogin` 의 성공 응답 = **"로그인 성공"**. refresh 자리에서 메시지 베껴오면 안 됨.

### L97: `log.info("_____", _____._____())` → `log.info("로그인 성공: m_id={}", membersVO.getM_id())`
- **왜 틀렸나**: 메시지·인자 둘 다 미작성.
- **외우는 법**: 성공 직후 감사 로그 자리. 메시지는 "로그인 성공: m_id={}" 같은 식. 인자는 **DB에서 꺼낸 membersVO**의 m_id (사용자 입력 mvo 아님).

### L101: `log.error("예외 에러", _____._____(), _____)` → `log.error("예외: m_id={}", mvo.getM_id(), e)`
- **왜 틀렸나**: 두 번째·세 번째 인자 미작성. 메시지(`"예외 에러"`)는 의미 동일이라 ✅.
- **외우는 법**: `log.error` 의 마지막 인자는 항상 **예외 객체 `e`**. 안 박으면 스택트레이스 안 찍힘. 패턴: `log.error("메시지", 변수, e)`.

---

## MembersService.java (4/4)

전부 정답 ✅. `MembersVO findById(String id);`

---

## MembersServiceImpl.java (9/9)

전부 정답 ✅. `public MembersVO findById(String id) { return membersMapper.findById(id); }`

---

## MembersMapper.java (4/4)

전부 정답 ✅. `MembersVO findById(String id);`

---

## members-mapper.xml (12/13)

### L13: `select * from members when m_active = 0 ...` → `... where ...`
- **왜 틀렸나**: SQL 키워드 오타. `when` 은 SQL 의 case 표현 안에서만 쓰는 키워드. 조건절 시작은 **`where`**.
- **외우는 법**: SQL 조건절 = `where` 무조건. `when` 은 `CASE WHEN ... THEN ... END` 안에서만.
