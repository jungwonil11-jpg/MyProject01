# 2026-05-16 GuestBookVO 100%

단일 파일 모드. **원본 구멍 뚫기 방식**.

## 학습 대상 파일

`src/main/java/com/study/myproject01/guestbook/vo/GuestBookVO.java`

## 빈칸 자리 (17개)

| 카테고리 | 개수 | 내용 |
|---|---|---|
| Lombok 어노테이션 | 3 | `@Data`, `@AllArgsConstructor`, `@NoArgsConstructor` |
| `private` 접근제어자 | 2 | 필드 선언 2줄 |
| 필드 타입 | 2 | `String`, `MultipartFile` |
| 필드명 | 10 | `g_idx`, `g_writer`, `g_subject`, `g_email`, `g_pwd`, `g_content`, `g_regdate`, `g_active`, `f_name`, `file_name` |

## 100% 룰

- 빈칸 풀 전부 + 변수명/접근제어자까지 다 비움
- VO 파일은 메소드가 없어서 빈칸 풀이 작지만, **VO 학습 의도(어노테이션·타입·필드명 외우기) 우선**으로 클래스 레벨 어노테이션과 필드 선언부도 빈칸 풀에 포함

## 학습 포인트

- `@Data` = getter/setter/toString/equals/hashCode 묶음
- `@AllArgsConstructor` = 모든 필드 받는 생성자 (수동 인스턴스화용)
- `@NoArgsConstructor` = **파라미터 없는 기본 생성자 — MyBatis가 reflection으로 객체 만들 때 필수**
- `private` × 2 = 캡슐화 (Lombok이 생성하는 getter/setter로 접근)
- `String` = DB 컬럼 타입이 섞여 있어도 자바 VO는 단일 타입(`String`)으로 받는 패턴
- `MultipartFile` = 파일 업로드용 Spring Web 타입. DB 저장은 별도 (`f_name` 컬럼으로 파일명만)
- 필드명은 DB 컬럼명과 정확히 같아야 MyBatis 자동 매핑됨 (`g_idx` ↔ `g_idx` 등)

## 안내

IntelliJ에서 `src/main/java/com/study/myproject01/guestbook/vo/GuestBookVO.java` 열고 `_____` 채우기. Lombok import 다 살아있어서 자동완성 동작.
끝나면 `/연습 채점`.
