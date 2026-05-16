# 채점 결과 #1 — 2026-05-15 GuestBook 리스트 67%

## 점수
**33 / 34 (97%)**

| 파일 | 점수 |
|---|---|
| GuestBookController.java | 22 / 23 |
| GuestBookService.java | 2 / 2 |
| GuestBookServiceImpl.java | 3 / 3 |
| GuestBookMapper.java | 2 / 2 |
| guestbook-mapper.xml | 4 / 4 |

> 5-파일 묶음 메소드명을 `getGuestBooklist`로 6곳(Controller 호출 + Service + ServiceImpl @Override + ServiceImpl 내부 mapper 호출 + Mapper + XML id) 일관되게 박았음. 강사 코드는 `guestBookList`이지만 5-파일 일관성 룰로 전부 정답 처리.

---

## GuestBookController.java (22 / 23)

### L32: `e.Message()` → `e.getMessage()`
- **왜 틀렸나**: 자바 메소드명은 case-sensitive. `Message()`(대문자 M)는 존재하지 않음. Exception 객체에서 메시지 꺼내는 표준 메소드는 `getMessage()` (소문자 g로 시작 + `get` 접두사). 컴파일 에러
- **외우는 법**: 값 꺼내는 메소드는 무조건 `get` 시작 + camelCase. Lombok이 만드는 모든 getter도 동일 (`getId()`, `getName()`). 대문자로 시작하는 메소드는 자바에 없음

---

## ⚠️ 빈칸 외 추가 이슈

### GuestBookServiceImpl.java L16: `getGuestBooklist()()` 괄호 중복
```java
public List<GuestBookVO> getGuestBooklist()() {
```
- 메소드 시그니처에 `()`가 두 번 박혀있음. 빈칸 정답(메소드명)은 맞췄는데 그 뒤에 `()`를 추가로 박은 것 같음 — 컴파일 에러
- 정답: `public List<GuestBookVO> getGuestBooklist() {` (괄호 1개만)
- 빈칸 채점 룰상 메소드명 자리는 정답이라 ✓ 처리했지만 실제 동작 안 됨 → IDE에서 `()` 하나 제거 필요
