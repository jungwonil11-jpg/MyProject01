# 2026-05-15 GuestBook 등록 — 67%

## 회차 정보
- **도메인**: GuestBook
- **기능**: 방명록 등록 (`POST /guestbook/insert`)
- **난이도**: 67% (P1~P7 자리 빈칸)
- **모드**: 5-파일 묶음

## 빈칸 카테고리

| 파일 | 빈칸 자리 |
|---|---|
| GuestBookController.java | `getGuestBookInsert` 메소드 본문 — 어노테이션·파라미터, 리턴 타입, `@RequestBody`, 파라미터 타입, `new`/생성자, 변수 타입, 서비스 호출명, setter, `Boolean.TRUE/FALSE`, 사용자 메시지 문자열, catch 타입, `getMessage` |
| GuestBookService.java | `guestBookInsert` 시그니처 — 리턴 타입, 메소드명, 파라미터 타입 |
| GuestBookServiceImpl.java | `guestBookInsert` `@Override` — 리턴 타입, 메소드명, 파라미터 타입, 매퍼 호출명 |
| GuestBookMapper.java | `guestBookInsert` 시그니처 — 리턴 타입, 메소드명, 파라미터 타입 |
| guestbook-mapper.xml | `<insert>` 태그 — `id`, `parameterType`, 컬럼 6개, `#{...}` 파라미터 6개 (`now()` 포함) |

## 변수명/메소드명 (안 비움)
- `dataVO`, `result`, `gvo`, `e`, `guestBookService`, `guestBookMapper`
- 컨트롤러 메소드명 `getGuestBookInsert` (자유 작명이라 33/67%에선 안 비움)

## 흐름 키워드 (안 비움)
- `==`, `0`, `return` — P8은 67%에선 안 비움

## 진행 방법
1. IDE에서 5개 파일 열기
2. `_____` 자리 채우기
3. 끝나면 `/연습 채점`

## 5-파일 묶음 핵심 포인트
Controller가 호출하는 메소드명 ↔ Service 인터페이스 시그니처 ↔ ServiceImpl `@Override` ↔ Mapper 인터페이스 시그니처 ↔ Mapper XML `<insert id="...">` — **5곳 전부 정확히 같은 이름**이어야 동작함. 이게 이 묶음의 가장 큰 학습 포인트.
