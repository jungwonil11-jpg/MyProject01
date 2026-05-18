# 채점 결과 #1 — 2026-05-17 GuestBook 리스트+등록 100%

## 점수
**150 / 153 (98%)**

| 기능 | 점수 |
|---|---|
| 방명록 리스트 | 71/71 |
| 방명록 등록 | 79/82 |

| 파일 | 점수 |
|---|---|
| GuestBookController.java | 89/90 |
| GuestBookService.java | 7/7 |
| GuestBookServiceImpl.java | 18/18 |
| GuestBookMapper.java | 7/7 |
| guestbook-mapper.xml | 29/31 |

> 5곳 메소드명 일관성 체크: `getGuestBookList`, `getGuestBookInsert` 모두 Controller 호출 / Service 시그니처 / ServiceImpl `@Override` / Mapper 시그니처 / XML `id` 5곳 일치 — 동작 OK.

---

## GuestBookController.java (89/90)

### L50: `dataVO.setData(Boolean.FALSE);` → `dataVO.setSuccess(Boolean.FALSE);`
- **왜 틀렸나**: catch 블록은 예외(=실패) 상황임. 성공/실패 플래그는 `setSuccess`로 박아야 클라이언트가 실패임을 알 수 있음. `setData`는 응답 본문 데이터 자리라 의미가 완전 다름 — `setData(false)`가 들어가면 응답 JSON에 `"data": false`가 박혀버림.
- **외우는 법**: `setSuccess` = true/false 플래그, `setMessage` = 사람용 메시지, `setData` = 실제 페이로드. **catch에서는 무조건 `setSuccess(Boolean.FALSE)` + `setMessage(e.getMessage())` 2종 세트**. try 끝의 else와 패턴 같음 — `setSuccess` 먼저, `setMessage` 다음, `setData`는 성공 분기에서만.

---

## guestbook-mapper.xml (29/31)

### L10: `parameterType="gvo"` → `parameterType="GuestBookVO"`
- **왜 틀렸나**: `parameterType`은 **클래스(타입) 이름**을 받음. MyBatis가 이 클래스 정보를 가지고 getter(`getG_writer()` 등)를 호출해서 `#{필드명}` 자리에 값을 채움. `gvo`는 자바 코드의 변수명일 뿐이라 MyBatis는 그 변수의 타입을 모름 → 매핑 실패.
- **외우는 법**: **parameterType = 클래스, `#{필드명}` = 그 클래스의 getter**. 자바에서 `gvo.getG_writer()`로 꺼내는 걸 XML에선 `parameterType="GuestBookVO"` + `#{g_writer}`로 표현. 변수명이 들어갈 자리는 **없음**.

### L12: `guestbook(#{g_content}, ...)` → `values(#{g_content}, ...)`
- **왜 틀렸나**: SQL INSERT 문법은 `INSERT INTO 테이블(컬럼) VALUES(값)`. `values` 키워드 자리에 테이블명을 또 박으면 SQL 파서가 문장을 이해 못 함 → `SyntaxError` 발생, 쿼리 실행 자체가 안 됨.
- **외우는 법**: **`INSERT INTO` ↔ `VALUES`는 짝꿍**. 영어 문장 그대로 "넣어라(INSERT) → 어디에(INTO 테이블) → 무엇을(VALUES 값)". 테이블명은 한 번만, 두 번째 라인은 무조건 `values(...)`.
