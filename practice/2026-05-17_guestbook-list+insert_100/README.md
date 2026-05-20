# 2026-05-17 GuestBook 리스트+등록 100%

## 회차 정보
- 시작: 2026-05-17
- 도메인: GuestBook
- 기능: 방명록 리스트, 방명록 등록
- 난이도: **100%** (빈칸 풀 전부 + 변수명·메소드명·`public`까지)

## 학습 대상 파일 (IntelliJ에서 열고 작업)
1. `src/main/java/com/study/myproject01/guestbook/controller/GuestBookController.java`
2. `src/main/java/com/study/myproject01/guestbook/service/GuestBookService.java`
3. `src/main/java/com/study/myproject01/guestbook/service/GuestBookServiceImpl.java`
4. `src/main/java/com/study/myproject01/guestbook/mapper/GuestBookMapper.java`
5. `src/main/resources/mapper/guestbook-mapper.xml`

## 빈칸 카테고리
선택 기능(list, insert) 메소드의 빈칸 풀 토큰 전부 + 변수명·메소드명·`public` 접근제어자까지.

빈칸 X (그대로 유지된 자리):
- 구조 키워드 (`try`, `catch`, `if`, `else`)
- 구두점/연산자 (`{`, `}`, `(`, `)`, `;`, `=`, `,`, `.`, `@`)
- 패키지 / import 구문
- 클래스 선언 + 클래스 레벨 어노테이션 (`@RestController`, `@RequestMapping("/guestbook")`)
- 필드 선언부 (`@Autowired private GuestBookService guestBookService;`)
- 선택 외 기능 (Controller의 `getGuestBookDetail`, Service/Mapper의 `guestBookDetail/Delete/Update`)

## 진행 방법
1. IntelliJ에서 위 5개 파일 열고 `_____` 채움
2. **5-파일 묶음 메소드명은 5곳이 전부 일치해야 동작**
   - Controller 호출 (`guestBookService.XXX()`)
   - Service 인터페이스 시그니처
   - ServiceImpl `@Override` 시그니처
   - Mapper 인터페이스 시그니처
   - mapper.xml `<select id="XXX">` / `<insert id="XXX">`
3. 끝나면 `/연습 채점`

## 채점 후
- 사용자 작성본은 회차 폴더 안에 `src/...` 트리로 보존됨
- `SCORE_1.md`에 오답만 정리 (정답·왜 틀렸나·외우는 법)
- src/ 는 원본(강사 코드)으로 자동 복구
