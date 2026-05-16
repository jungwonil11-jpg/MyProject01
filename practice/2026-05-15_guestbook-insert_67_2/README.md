# 2026-05-15 GuestBook 등록 — 67% (2회차)

> 이전 회차: `2026-05-15_guestbook-insert_67/` (SCORE_1.md = 27/47, 57%). 같은 기능 재도전.

## 회차 정보
- **도메인**: GuestBook
- **기능**: 방명록 등록 (`POST /guestbook/insert`)
- **난이도**: 67%
- **모드**: 5-파일 묶음

## 빈칸 카테고리

| 파일 | 빈칸 자리 | 개수 |
|---|---|---|
| GuestBookController.java | `getGuestBookInsert` 본문 — 어노테이션·파라미터, 리턴 타입, `@RequestBody`, 파라미터 타입, `new`/생성자, 변수 타입, 서비스 호출명, setter, `Boolean.TRUE/FALSE`, 사용자 메시지 문자열, catch 타입, `getMessage` | 23 |
| GuestBookService.java | `guestBookInsert` 시그니처 — 리턴 타입, 메소드명, 파라미터 타입 | 3 |
| GuestBookServiceImpl.java | `guestBookInsert` `@Override` — 리턴 타입, 메소드명, 파라미터 타입, 매퍼 호출명 | 4 |
| GuestBookMapper.java | `guestBookInsert` 시그니처 — 리턴 타입, 메소드명, 파라미터 타입 | 3 |
| guestbook-mapper.xml | `<insert>` 태그 — `id`, `parameterType`, 컬럼 6개, `#{...}` 값 6개 | 14 |
| **합계** | | **47** |

## 1회차 피드백 (놓친 포인트)
- `@RequestBody DataVO gvo` — `DataVO`는 응답용. 요청 본문 타입은 도메인 VO(`GuestBookVO`)
- `result == 0` 분기 = **실패** → `Boolean.FALSE`, "등록 실패"
- catch 블록 = **무조건 실패** → `Boolean.FALSE`
- `e.Message()` 같은 대문자 시작 메소드 없음. `e.getMessage()`
- `catch (exception e)` 소문자 X. `Exception` (대문자)
- ServiceImpl `@Override` 리턴 타입은 Service 인터페이스와 정확히 일치 (`int`)
- XML INSERT는 컬럼 리스트와 `#{필드명}` 값 리스트 1:1 매칭 (`g_regdate`만 `now()`)

## 진행 방법
1. IDE에서 5개 파일 열기
2. `_____` 자리 채우기 (총 47개)
3. 끝나면 `/연습 채점`

막히면 `/힌트`.
