# 2026-05-17 Members 마이페이지 100%

## 회차 정보
- 시작: 2026-05-17
- 도메인: Members
- 기능: 마이페이지 (`GET /members/myPage`)
- 난이도: **100%** — 구두점/괄호/`@`/`;`/`.`/`,`/`=` + 데이터성 문자열 빼고 전부 빈칸

## 학습 대상 파일 (IntelliJ에서 `_____` 채우기)

1. `src/main/java/com/study/myproject01/members/controller/MembersController.java` (`getMyPage` 메소드)
2. `src/main/java/com/study/myproject01/members/service/MembersService.java`
3. `src/main/java/com/study/myproject01/members/service/MembersServiceImpl.java`
4. `src/main/java/com/study/myproject01/members/mapper/MembersMapper.java`
5. `src/main/resources/mapper/members-mapper.xml`

## 기능: 마이페이지 조회

- **URL**: GET `/members/myPage`
- **요청**: 없음 (JWT 토큰으로 인증 — JwtRequestFilter에서 SecurityContextHolder에 userId 저장)
- **응답**: DataVO { success, message, data: MembersVO }

## 100% 빈칸 룰

**전부 빈칸 (`_____` 5밑줄 통일)**:
- 어노테이션 이름: `GetMapping` → `_____` (`@` 기호는 유지)
- 어노테이션 파라미터: `/myPage` → `_____` (URL 경로)
- 접근제어자: `public` → `_____`
- 시그니처 리턴 타입·메소드명: `DataVO getMyPage` → `_____ _____`
- 변수 타입·변수명: `DataVO dataVO` → `_____ _____`
- 생성자: `new DataVO()` → `_____ _____()`
- 구조 키워드: `try`, `catch`, `if`, `else` → `_____`
- 흐름 키워드: `return` → `_____`
- 클래스명 캐스팅: `(String)` → `(_____)`
- 표준 라이브러리 클래스명·메소드명: `SecurityContextHolder.getContext()` → `_____._____()`
- 메소드 호출명: `findById`, `setSuccess`, `setMessage`, `setData` → `_____`
- 조건 연산자: `==` → `_____`
- 값 토큰: `null`, `Boolean.TRUE`, `Boolean.FALSE` → `_____` 또는 `_____._____`
- 예외 변수명: `e` → `_____`

**유지 (모든 비율 공통 빈칸 X)**:
- 구두점/괄호/할당: `{`, `}`, `(`, `)`, `;`, `=`, `,`, `.`, `:`, `@`
- 패키지·import·클래스 선언·필드 선언
- **데이터성 문자열 리터럴**: `"없는 아이디 입니다."`, `"마이페이지 성공"` (사용자 메시지)
- 다른 기능 메소드 (로그인, 토큰 재발급)

> ⚠️ 어노테이션 경로 `"/myPage"`는 데이터가 아닌 식별자 → 따옴표 안만 빈칸 (`"_____"`)

## 진행 방법

1. IntelliJ에서 5개 파일 열기
2. `// mypage : ...` 주석부터 `return _____;` 까지 `_____` 채우기
3. 비즈니스 로직 추적:
   - **Controller**: SecurityContextHolder에서 userId 추출 → Service 호출
   - **Service**: Mapper 호출 (메소드 시그니처만 정의)
   - **ServiceImpl**: Mapper 메소드 호출
   - **Mapper**: XML 쿼리 호출
   - **XML**: userId 조건으로 members 테이블 조회 (m_active=0)
4. 끝나면 `/연습 채점` 실행

## 채점 룰 요약

- **자유 작명 자리** (5-파일 메소드명, 변수명, `getMyPage`) → 5곳/메소드 내 일관성으로 O/X
- **고정 의미 자리** (Java 키워드, 어노테이션 이름, 표준 라이브러리) → 완전 일치
- **의미 동등 자리** (`==`, `Boolean.FALSE` ↔ `false`) → 동작 같으면 O

## 팁

- SecurityContextHolder 부분은 토큰 검증이 완료된 상태. userId는 바로 String으로 캐스팅 가능.
- `setSuccess`/`setMessage`/`setData`는 DataVO의 setter (Lombok 생성). 완전 일치 채점.
- members 테이블에서 m_active=0인 행을 SELECT — "활성 사용자"를 의미.
