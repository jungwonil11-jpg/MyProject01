# 채점 결과 #1 — 2026-05-16 GuestBookVO 100%

> ⚠️ 채점 정정 — 이전에 시스템 reminder의 옛 스냅샷 기반으로 잘못 채점했음. 실제 답안 보존본(`src/...`) 다시 읽고 재채점.

## 점수
**13 / 17 (76.5%)**

| 파일 | 점수 |
|---|---|
| GuestBookVO.java | 13/17 |

---

## GuestBookVO.java (13/17)

### L12: `g_dix` → `g_idx`
- **왜 틀렸나**: 오타. `i`와 `d` 위치 바뀜. DB 컬럼명은 `g_idx`임. MyBatis가 SELECT 결과를 VO에 매핑할 때 **컬럼명 ↔ 필드명 일치 기준**으로 자동 매핑함. `g_dix`라는 컬럼이 DB에 없으므로 그 필드는 `null`로 남음. 컴파일은 통과지만 런타임에 PK 값이 매번 null로 들어옴.
- **외우는 법**: `idx` = "**i**n**d**e**x**" 줄임말 (인덱스/식별자). `dix`는 영어 단어 자체가 없음. "ID-X"로 외우면 헷갈림 적음.

### L13: `return _____ _____;` → `private MultipartFile file_name;`
- **왜 틀렸나**: 클래스 본문에 직접 `return` 키워드가 들어감 → 컴파일 에러 `illegal start of expression`. `return`은 메소드 안에서만 의미. 빈칸 자리는 **필드 선언** (`private 타입 변수명;`).
- **외우는 법**: 클래스 본문에 직접 들어가는 건 4가지뿐 — **① 필드 선언, ② 메소드, ③ 생성자, ④ 정적 블록**. 그 외(`return`, `if`, `for` 등 흐름 제어)는 다 메소드 안에서만.

### L13 `MultipartFile` 자리: 미작성 → `MultipartFile`
- **왜 틀렸나**: 미작성. 위 `return` 잘못 박는 바람에 나머지 빈칸 두 개 손도 못 댐.
- **외우는 법**: 파일 업로드 받는 자리 = 무조건 `MultipartFile` (Spring Web 타입). multipart/form-data 요청의 파일 부분.

### L13 `file_name` 자리: 미작성 → `file_name`
- **왜 틀렸나**: 같은 이유 (미작성).
- **외우는 법**: 파일 객체 자체 = `file_name`, DB 저장용 파일명 문자열 = `f_name` (위 줄 필드). 다른 변수임.

---

## 잘한 부분 (학습 가치 있는 것만 짚음)

- **어노테이션 3개 다 정답**. 순서가 원본과 달라도 컴파일러는 어노테이션 순서 안 봄 → 동작 동일 → O (3개). `@AllArgsConstructor`도 자동완성으로 확정 잘 함.
- **필드명 8개 정답** (`g_writer`, `g_subject`, `g_email`, `g_pwd`, `g_content`, `g_regdate`, `g_active`, `f_name`). 순서가 원본과 살짝 다르지만 자바 클래스 필드는 선언 순서가 동작에 영향 X, MyBatis 매핑도 필드명 매칭이지 순서 매칭 아님 → 다 O.

## 핵심

오답 4개 중 3개가 한 줄(L13) 통째로 잘못 박은 거. 그 줄에 `private MultipartFile file_name;` 박았으면 16/17 (94%)였음.
**클래스 본문 = 필드 선언 자리** 인식이 핵심. `return`이 그 자리에 못 들어간다는 것만 외우면 충분.
