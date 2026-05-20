# 채점 결과 #1 — 2026-05-17 Members 마이페이지 100%

## 점수
**59 / 68 (87%)**

| 기능 | 점수 |
| --- | --- |
| 마이페이지 | 59/68 |

| 파일 | 점수 |
| --- | --- |
| MembersController.java | 45/50 |
| MembersService.java | 3/4 |
| MembersServiceImpl.java | 7/9 |
| MembersMapper.java | 3/4 |
| members-mapper.xml | 1/1 |

---

## MembersController.java (45/50)

### L103: `findById` → `getMyPage`
- **왜 틀렸나**: 컨트롤러 메소드명을 서비스 메소드명(`findById`)으로 짜버렸음. 같은 클래스에 이미 `findById`라는 식별자 흐름이 있는데 메소드명까지 같으면 가독성 폭망 + 서비스 메소드와 헷갈림. 100% 모드에선 컨트롤러 메소드명도 채점 대상.
- **외우는 법**: 컨트롤러 메소드명은 **URL 의도**를 그대로 박음. `/myPage` → `getMyPage`. URL 토큰이 메소드명 힌트임.

### L108: `findbyId` → `findById`
- **왜 틀렸나**: 5-파일 일관성에서 `b`가 소문자로 깨짐. Controller·Service·ServiceImpl·Mapper 4곳에서 `findbyId`(소문자b)인데 XML만 `findById`(대문자B). MyBatis가 매퍼 인터페이스의 `findbyId`를 XML id `findById`로 매칭 못 함 → `BindingException` 터짐.
- **외우는 법**: Java 카멜케이스 규칙 — `findBy + Id` = `findById`. `By`도 대문자 시작. 5곳 중 4곳을 다 바꿔도 XML 하나 못 바꾸면 다 망함. 한 군데 고치면 grep으로 5곳 다 동기화.

### L108: `String id` → `userId`
- **왜 틀렸나**: 메소드 호출 인자 자리에 변수 선언을 박았음. `findbyId(String id)`는 인자 자리 문법이 아니라 메소드 선언 자리 문법임. 컴파일 에러 (`error: <identifier> expected`).
- **외우는 법**: 호출 vs 선언 구분. **호출**: `메소드(값)` — 값 자리엔 변수 그대로 (`userId`). **선언**: `리턴 메소드(타입 이름)` — 시그니처에만 타입 박음.

### L109: `userId` → `mvo`
- **왜 틀렸나**: null 비교 대상이 잘못됨. `userId`는 SecurityContextHolder에서 막 꺼낸 토큰 주인 ID라 null일 수 없음(JwtRequestFilter가 통과시킨 후니까). 정작 null 검사해야 할 건 **DB 조회 결과** `mvo` — 토큰 ID가 DB에 없으면(탈퇴/삭제) null 떨어짐.
- **외우는 법**: "DB 조회 후엔 결과부터 null 체크". `service.findXxx()` 직후 if문은 무조건 그 결과를 검사. SecurityContext에서 꺼낸 값은 필터가 검증한 상태라 null 체크 불필요.

### L115: `userId` → `mvo`
- **왜 틀렸나**: 같은 이유 — 응답 데이터 자리에 토큰 ID 문자열만 박았음. 마이페이지의 핵심은 **회원 정보 전체**(이메일·이름·가입일 등)인데 `userId` 문자열 하나만 보내면 클라이언트가 마이페이지 화면 못 그림. `setData(mvo)`로 회원 객체 통째 보내야 의미 있음.
- **외우는 법**: 응답 `setData`엔 **클라이언트가 화면 그릴 데이터**를 박음. 마이페이지면 회원 정보 객체, 리스트면 List, 단건 조회면 VO.

---

## MembersService.java (3/4)

### L8: `findbyId` → `findById`
- **왜 틀렸나**: 같은 이유 — 카멜케이스 깨짐. 인터페이스 시그니처가 깨지면 Controller가 호출하는 메소드명도 같이 깨져서 4곳이 도미노로 틀어짐.
- **외우는 법**: 같은 이유.

---

## MembersServiceImpl.java (7/9)

### L15: `findbyId` → `findById`
- **왜 틀렸나**: `@Override` 메소드 시그니처가 인터페이스랑 일치는 하는데(둘 다 깨진 이름), XML id와 매칭 안 됨. 같은 이유.
- **외우는 법**: 같은 이유.

### L16: `findbyId` → `findById`
- **왜 틀렸나**: 매퍼 호출명. ServiceImpl의 메소드명을 `findbyId`로 박으면 매퍼 인터페이스의 `findbyId(String id)`를 호출하게 됨. 거기까진 컴파일 OK인데 매퍼 인터페이스의 `findbyId`를 XML이 `findById`로 들고 있어서 런타임에 `BindingException`.
- **외우는 법**: 같은 이유.

---

## MembersMapper.java (3/4)

### L10: `findbyId` → `findById`
- **왜 틀렸나**: 매퍼 인터페이스 메소드명. XML의 `<select id="...">` 값과 **정확히 같아야** MyBatis가 매칭함. 4곳이 깨졌으니 1곳(XML)이 외톨이 됨.
- **외우는 법**: 같은 이유. MyBatis는 매퍼 인터페이스 메소드명 ↔ XML id를 **문자열 매칭**함. 대소문자 1글자만 달라도 fail.

---

## members-mapper.xml (1/1)
오답 없음. `<select id="findById">` 그대로 유지함.

---

## 종합 코멘트
- 오답 5건 중 4건이 **같은 뿌리(`findById` 카멜케이스)**임. 한 군데 고치면 5곳 다 grep으로 동기화하는 습관 필요.
- 나머지 1건(`userId` vs `mvo`)은 **DB 조회 결과 null 체크 + 응답 데이터 선택** 패턴 미숙. 마이페이지 같은 "내 정보 조회" 패턴은 토큰 ID로 DB 한 번 더 조회해서 객체 전체를 응답에 박는 게 정석.
- 문자열 메시지 `"없음"`/`"있음"`은 의미는 통하지만 사용자 메시지로는 약함 — 정답대로 `"없는 아이디 입니다."`/`"마이페이지 성공"` 처럼 상황을 명확히 담아주는 게 좋음.
