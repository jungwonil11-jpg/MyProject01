# 2026-05-15 GuestBook 리스트 — 67%

## 회차 정보
- **도메인**: GuestBook
- **기능**: 방명록 리스트 (`GET /guestbook/list`)
- **난이도**: 67% (P1~P7 자리 빈칸)
- **모드**: 5-파일 묶음

## 빈칸 카테고리

| 파일 | 빈칸 자리 | 개수 |
|---|---|---|
| GuestBookController.java | `getGuestBooklist` 본문 — 어노테이션·파라미터, 리턴 타입, 변수 타입, 생성자, 서비스 호출명, getter/setter, `Boolean.TRUE/FALSE`, 메시지 문자열, catch 타입, `getMessage` | 23 |
| GuestBookService.java | `guestBookList` 시그니처 — 리턴 타입, 메소드명 | 2 |
| GuestBookServiceImpl.java | `guestBookList` `@Override` — 리턴 타입, 메소드명, 매퍼 호출명 | 3 |
| GuestBookMapper.java | `guestBookList` 시그니처 — 리턴 타입, 메소드명 | 2 |
| guestbook-mapper.xml | `<select>` 태그 — `id`, `resultType`, 테이블명, WHERE 컬럼명 | 4 |
| **합계** | | **34** |

## 변수명/메소드명 (안 비움)
- `dataVO`, `gustbookList`, `e`, `guestBookService`, `guestBookMapper`
- 컨트롤러 메소드명 `getGuestBooklist` (자유 작명이라 33/67%에선 안 비움)

## 흐름 키워드 (안 비움)
- `==`, `null`, `||`, `return` — P8은 67%에선 안 비움

## 진행 방법
1. IDE에서 5개 파일 열기
2. `_____` 자리 채우기 (총 34개)
3. 끝나면 `/연습 채점`

## 5-파일 묶음 핵심
Controller 호출 → Service 인터페이스 → ServiceImpl `@Override` → Mapper 인터페이스 → Mapper XML `<select id="...">` — **5곳 전부 같은 이름**이어야 동작. 강사 코드는 `guestBookList`로 박혀있지만 본인이 일관되게 다른 이름 박아도 정답 인정됨 (예: `guestbookList` 같은 식).

막히면 `/힌트`.
