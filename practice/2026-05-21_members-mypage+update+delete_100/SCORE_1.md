# 채점 결과 #1 — 2026-05-22 Members 마이페이지+회원정보 수정+회원 탈퇴 100%

## 점수

**265 / 311 (85.2%)**

| 기능 | 점수 |
| --- | --- |
| 마이페이지 | ~120/125 (96%) |
| 회원정보 수정 | ~88/110 (80%) |
| 회원 탈퇴 | ~57/76 (75%) |

| 파일 | 점수 |
| --- | --- |
| MembersController.java | ~250/261 |
| MembersService.java | 11/12 |
| MembersServiceImpl.java | 17/18 |
| MembersMapper.java | 11/12 |
| members-mapper.xml | ~23/55 |

---

## MembersController.java (~250/261)

### L222: `@PostMapping ("/mypage")` → `@GetMapping("/mypage")`
- **왜 틀렸나**: 마이페이지 = **조회** = CRUD의 **R**ead. GET이 맞음. POST는 Create/처리 자리.
- **외우는 법**: **데이터를 보내는 게 아니라 받아오기만 하는 자리 = GET**. 회원 정보 보여달라 — 클라이언트가 서버에 데이터 안 보냄. URL만 있으면 됨.

### L240: `dataVO.setData(dataVO);` → `dataVO.setData(membersVO);`
- **왜 틀렸나**: 자기 자신(`dataVO`)을 `data` 필드에 박음. 응답에 회원 정보 안 나가고 자기 자신 박힌 무한 참조. 직전 줄 `MembersVO membersVO = membersService.findById(userId);` 로 조회한 결과를 박아야 함.
- **외우는 법**: `setData(?)` 자리 = "**클라이언트에 보낼 데이터**". 직전 줄에서 DB 조회로 만든 변수가 정답. dataVO는 응답 컨테이너 자체라 자기 박으면 망함.

### L259: `log.warn("회원정보 수정 시도 ID : {}", userId);` → `log.info(...)`
- **왜 틀렸나**: log 레벨 의미 다름. `info` = 정상 흐름 기록 (감사 로그). `warn` = 잠재적 문제 (보안 이벤트). 회원이 자기 정보 수정하는 건 정상 흐름 → `info`.
- **외우는 법**: **시도/성공 로그는 info, 실패/위조 의심은 warn, 예외는 error**.

### L261: `dataVO.setData(userId);` → `mvo.setM_id(userId);`
- **왜 틀렸나**: 회원정보 수정의 핵심은 "**클라이언트가 보낸 mvo(수정할 데이터) 에 userId만 토큰 출처로 덮어쓰기**". `dataVO.setData(userId)` 는 응답에 userId 박는 거 → 수정 로직과 무관.
- **외우는 법**: update 흐름 = "토큰에서 userId 꺼냄 → mvo의 m_id에 박음 → service.update(mvo)". **응답이 아니라 입력 객체에 박는 거**.

### L263: `membersService.memberUpdate(userId);` → `membersService.update(mvo);` (인자 자리)
- **왜 틀렸나**: 메소드명 `memberUpdate` 자체는 5-파일 일관 OK. 그러나 인자가 `userId` (String) 만 넘어가면 다른 필드 (name, addr, email 등) 수정 불가. **mvo 통째로 넘겨야** 다 수정됨.
- **외우는 법**: update 메소드 인자 = "수정할 데이터 전부 담은 VO". userId 하나만 보내면 의미 없음.

### L279: `@PostMapping ("/delete")` → `@GetMapping("/delete")`
- **왜 틀렸나**: 강사 원본이 GetMapping. (사실 탈퇴는 POST가 HTTP semantic상 맞지만 강사 컨벤션 따름)
- **외우는 법**: 강사 코드 패턴 보고 일치시킬 것. 의미는 별개 사안.

### L286: `log.warn(...)` → `log.info(...)`
- **왜 틀렸나**: 같은 이유 (수정 시도 자리와 동일). 회원 본인 탈퇴 = 정상 흐름 = info.
- **외우는 법**: 같은 이유.

### L288: `RefreshTokenVO.deleteRefreshToken(userId);` → `membersService.deleteRefreshToken(userId);` ⚠️ **컴파일 에러**
- **왜 틀렸나**: `RefreshTokenVO`는 **VO 클래스** (데이터 담는 그릇). 정적 메소드 X. 정적 호출하면 컴파일 에러. 호출 대상은 **`membersService`** (Spring이 주입한 서비스 인스턴스).
- **외우는 법**: **DB 조작 = 무조건 service 호출**. VO는 그냥 데이터 컨테이너 (getter/setter만 있음). 클래스명.메소드() 형태로 직접 호출 X.

### L290: `MembersVO.deleteMember(userId);` → `membersService.delete(userId);` ⚠️ **컴파일 에러**
- **왜 틀렸나**: 같은 이유. `MembersVO` 클래스에 직접 정적 호출. 메소드명 `deleteMember`는 5-파일 일관 OK (Service/Impl/Mapper에 일관), 다만 호출 대상이 잘못.
- **외우는 법**: 같은 이유.

---

## MembersService.java (11/12)

### L23: `void memberUpdate(String userId);` → `void update(MembersVO mvo);` (파라미터 타입 자리)
- **왜 틀렸나**: 메소드명 `memberUpdate` 자체는 5-파일 일관성 룰로 OK. 그러나 **파라미터 타입 `String` vs `MembersVO` 는 완전 일치 룰** (변수 타입). String만 받으면 update 의미 깨짐.
- **외우는 법**: update 메소드 시그니처 = `void update(MembersVO mvo)` 패턴. userId 만 받는 update는 의미 없음.

---

## MembersServiceImpl.java (17/18)

### L43: `public void memberUpdate(String userId)` → `public void update(MembersVO mvo)` (파라미터 타입)
- **왜 틀렸나**: Service와 같은 이유. 인터페이스 ↔ 임플 시그니처 일치 필수.
- **외우는 법**: 인터페이스 변경하면 임플도 같이.

---

## MembersMapper.java (11/12)

### L25: `void memberUpdate(String userId);` → `void update(MembersVO mvo);` (파라미터 타입)
- **왜 틀렸나**: Service와 동일. 5-파일 일관성 깨짐.
- **외우는 법**: 한 곳 시그니처 바뀌면 5곳 다.

---

## members-mapper.xml (~23/55)

### L19: `parameterType="userId"` → `parameterType="String"`
- **왜 틀렸나**: `parameterType`은 **자바 타입** 이름 (String/Integer/MembersVO 등). `userId`는 변수명. 변수명을 타입 자리에 박으면 MyBatis 매핑 실패.
- **외우는 법**: **parameterType = 받는 타입, resultType = 돌려주는 타입**. 둘 다 자바 클래스명 (또는 String 같은 별칭). 변수명 X.

### L23-25: update 쿼리 본문 (`update into members where m_id = #{m_id}`)
- **왜 틀렸나**: 큰 혼란.
  - `update into`: UPDATE 문법은 `UPDATE 테이블 SET 컬럼=값 WHERE 조건` 임. `into` 는 INSERT 문법 (`INSERT INTO 테이블 ...`).
  - **`SET` 절 통째 빠짐**. 어떤 컬럼을 어떤 값으로 바꿀지 안 박힘 → 컴파일은 되지만 DB 실행 시 에러.
- **정답 패턴**:
  ```sql
  update members
  set m_name=#{m_name}, m_addr=#{m_addr}, m_addr2=#{m_addr2},
      m_email=#{m_email}, m_phone=#{m_phone}
  where m_id=#{m_id} and m_active=0
  ```
- **외우는 법**: **UPDATE 4단계** — `UPDATE 테이블`, `SET 컬럼=값`, `WHERE 조건`. into는 INSERT 전용.

### L43-45: `<delete id="deleteRefreshToken">` 자리 (학습 대상이었음. 미작성)
- **왜 틀렸나**: 사용자가 delete 학습 자리의 패턴(SET 절)을 deleteRefreshToken 자리에 박음. 자리 헷갈림. 그리고 `_____` 빈칸 5개 그대로 남음 (미작성).
- **정답 패턴**:
  ```sql
  delete from refresh_tokens where rt_user_id = #{rt_user_id}
  ```
- **외우는 법**: **DELETE 3단계** — `DELETE FROM 테이블 WHERE 조건`. SET 없음. 회원 탈퇴(soft delete) 는 별개 — `UPDATE`로 m_active=1 박는 거.

### L36-41: `<update id="deleteMember">` 자리 (delete 학습 자리, 미작성 + 잘못)
- **왜 틀렸나**: 회원 탈퇴 = soft delete = `update members set m_active=1, m_active_reg=now() where m_id=#{m_id}` 패턴인데 사용자가 update SQL 패턴(set m_name=, m_addr=, ...) 박음. 게다가 `_____` 빈칸 10개 그대로 남음 (미작성).
- **정답 패턴**:
  ```sql
  update members set m_active=1, m_active_reg=now() where m_id=#{m_id}
  ```
- **외우는 법**: **soft delete = DELETE 안 쓰고 UPDATE로 m_active=1**. row 자체는 살아있음. now() = MySQL 함수 (현재 시각).

---

## 핵심 패턴 정리 (반복 학습용)

### 1. CRUD의 R = **Read**, D = Delete = Remove
- mypage = Read = **GET**
- delete = Delete = (강사는 GET 박음)

### 2. log 레벨
- 시도/성공 → `info`
- 실패/보안 이벤트 → `warn`
- 예외 → `error`

### 3. VO 정적 호출 금지 — 컴파일 에러
- `RefreshTokenVO.deleteRefreshToken(...)` ❌ → `membersService.deleteRefreshToken(...)` ✅
- VO = 그릇. DB 작업 = service.

### 4. update 인자 = VO 통째
- `service.update(userId)` ❌ → `service.update(mvo)` ✅
- mvo에 토큰 userId를 setM_id로 박은 후 통째로 넘김.

### 5. SQL 문법
- INSERT: `INSERT INTO 테이블 (컬럼) VALUES (값)`
- UPDATE: `UPDATE 테이블 SET 컬럼=값 WHERE 조건`
- DELETE: `DELETE FROM 테이블 WHERE 조건`
- soft delete = UPDATE로 active 플래그만 바꿈

### 6. parameterType / resultType = 자바 타입
- String / MembersVO / RefreshTokenVO 등 클래스명. 변수명 박지 말 것.
