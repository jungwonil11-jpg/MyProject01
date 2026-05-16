# 채점 결과 #1 — 2026-05-15 GuestBook 등록 67% (2회차)

> 이전 회차: `2026-05-15_guestbook-insert_67/SCORE_1.md` (27/47, 57%). 이번 +13 향상.

## 점수
**40 / 47 (85%)**

| 파일 | 점수 |
|---|---|
| GuestBookController.java | 19 / 23 |
| GuestBookService.java | 3 / 3 |
| GuestBookServiceImpl.java | 3 / 4 |
| GuestBookMapper.java | 3 / 3 |
| guestbook-mapper.xml | 12 / 14 |

> 5-파일 묶음 메소드명을 `getGuestBookInsert`로 통일 시도함. 5/6곳 일관(Controller 호출, Service, ServiceImpl @Override 시그니처, Mapper, XML id) — 단 **ServiceImpl 내부 mapper 호출(L27)이 미작성**이라 그 자리만 ❌.

---

## GuestBookController.java (19 / 23)

### L44: `"불러오기실패"` → `"등록 실패"`
- **왜 틀렸나**: "불러오기"는 SELECT/조회 동작에 쓰는 단어. INSERT는 "등록". 사용자가 등록 버튼 눌렀는데 응답 메시지에 "불러오기 실패"가 뜨면 어떤 동작이 실패한 건지 헷갈림. 의미가 달라서 채점상 ❌
- **외우는 법**: 메시지 동사는 컨트롤러 메소드가 하는 동작을 그대로 따라가기. `insert` → "등록", `list` → "불러오기", `update` → "수정", `delete` → "삭제"

### L47: `"불러오기성공"` → `"등록 성공"`
- **왜**: 같은 이유 (L44 참고)

### L50: `dataVO.setSuccess("Boolean.FALSE")` → `dataVO.setSuccess(Boolean.FALSE)`
- **왜 틀렸나**: 따옴표 때문에 **문자열 "Boolean.FALSE"** 가 됨. `setSuccess`는 Boolean 타입 받는 메소드라 타입 불일치 컴파일 에러. `Boolean.FALSE`(따옴표 X)는 자바 클래스의 static 필드값
- **외우는 법**: 클래스명.필드명(`Boolean.TRUE`, `Boolean.FALSE`) 자체가 값임. 따옴표로 감싸면 그냥 글자가 됨. 코드 토큰 vs 문자열 구분 — 따옴표는 사람이 읽을 메시지에만

### L51: `e.setMessage()` → `e.getMessage()`
- **왜 틀렸나**: Exception 객체에서 메시지 **꺼내는** 동작이라 `get` 시작. `setMessage`는 값을 **넣는** 동작인데 인자도 없이 호출해서 컴파일 에러. 게다가 Exception에 `setMessage`는 표준으로 없음
- **외우는 법**: catch 블록에선 무조건 `e.getMessage()`. get은 꺼내기, set은 넣기 — 인자 유무가 결정타

---

## GuestBookServiceImpl.java (3 / 4)

### L27: `guestBookMapper._____(gvo)` → `guestBookMapper.getGuestBookInsert(gvo)`
- **왜 틀렸나**: 미작성. 5-파일 묶음 일관성의 마지막 한 자리. Controller·Service·ServiceImpl @Override·Mapper·XML 다 `getGuestBookInsert`로 통일했는데 여기 하나만 비어있음 → MyBatis가 호출할 메소드를 못 찾아서 NoSuchMethodError 또는 컴파일 에러
- **외우는 법**: 5-파일 묶음 풀 때 ServiceImpl `@Override` 본문의 mapper 호출도 같이 박는 자리임. 메소드 시그니처(`public int getGuestBookInsert(...)`)만 채우면 절반만 완성. 본문 안 `guestBookMapper.???(gvo)`도 같은 이름

---

## guestbook-mapper.xml (12 / 14)

### L11, L12: 컬럼 + 값 순서 — `g_idx` 포함했고 `g_content` 누락
- **사용자**: `(g_idx, g_writer, g_subject, g_email, g_pwd, g_regdate)` / `values(#{g_idx}, #{g_writer}, #{g_subject}, #{g_email}, #{g_pwd}, now())`
- **정답**: `(g_writer, g_subject, g_content, g_email, g_pwd, g_regdate)` / `values(#{g_writer}, #{g_subject}, #{g_content}, #{g_email}, #{g_pwd}, now())`
- **왜 틀렸나**:
  - **`g_idx`는 INSERT에서 제외**: PK auto-increment 컬럼이라 DB가 자동 채워줌. 명시하면 사용자 입력값으로 박혀서 충돌 또는 의도와 다른 동작
  - **`g_content` 빠짐**: 방명록 본문 필드인데 누락. 본문 없는 게시글이 됨
- **외우는 법**:
  - INSERT 컬럼 리스트는 **사용자 입력 필드만**. `_idx`(PK, 자동)와 `_active`(soft delete, 기본값 0)는 보통 제외
  - VO의 `private String g_idx, g_writer, g_subject, g_email, g_pwd, g_content, ...` 순서대로 보면 어떤 필드가 사용자 입력인지 감 잡힘. 항상 `GuestBookVO` 클래스 한 번 열어보고 들어가기
