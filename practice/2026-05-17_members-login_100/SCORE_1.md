# 채점 결과 #1 — 2026-05-17 Members 로그인 100%

## 점수
**90 / 192 (47%)**

| 파일 | 점수 |
|---|---|
| MembersController.java (`getLogin`) | 67/115 |
| MembersService.java | 4/12 |
| MembersServiceImpl.java | 9/25 |
| MembersMapper.java | 4/12 |
| members-mapper.xml | 6/28 |

> 5-파일 일관성: 사용자가 `findById`의 파라미터명을 `getM_idx`로 통일했지만 Service/Mapper 2곳만 일관, ServiceImpl까지만 적용. deleteRefreshToken/saveRefreshToken는 5곳 모두 미작성.

---

## MembersController.java (67/115)

### L50: `@GetMapping` → `@PostMapping`
- **왜 틀렸나**: 로그인은 비밀번호를 받는 요청. **POST가 표준** (URL에 비밀번호 안 노출). GET 쓰면 `?m_id=...&m_pw=...` 같이 URL에 평문 노출됨 → 보안 망함.
- **외우는 법**: "로그인·회원가입·민감정보 = POST" 한 줄로 박음. GET은 단순 조회만.

### L51: `@RequestBody String m_idx` → `@RequestBody MembersVO mvo`
- **왜 틀렸나**: 클라이언트가 JSON으로 `{"m_id": "...", "m_pw": "..."}` 두 필드 보냄. String 한 개로 받으면 m_id, m_pw 둘 다 못 받음. **VO 객체로 받아야 두 필드 다 들어옴**.
- **외우는 법**: `@RequestBody` 다음은 **JSON 구조를 통째로 받을 객체 타입**. 필드 2개 이상이면 무조건 VO.

### L55: `MembersVO mvo = membersService.findById(mvo.getM_idx());` → `MembersVO membersVO = membersService.findById(mvo.getM_id());`
- **왜 틀렸나** (3개 문제):
  1. **변수명 `mvo` 충돌**: 위 파라미터(L51)가 이미 `mvo`. 같은 이름 변수 또 만들면 컴파일 에러. 새 변수는 다른 이름 (강사: `membersVO`).
  2. **`getM_idx` → `getM_id`**: VO 컬럼명은 `m_id` (DB 컬럼). `m_idx`라는 컬럼 존재 X.
  3. 결국 `mvo.getM_idx()` 는 두 가지 다 틀림.
- **외우는 법**:
  - 파라미터로 들어온 변수와 **다른 이름**으로 내부 변수 작성: `mvo` (파라미터) → `membersVO` (DB에서 가져온 거)
  - DB 컬럼 = VO 필드. `m_id`는 DB의 `m_id` 컬럼 → getter `getM_id()`.

### L58: `Boolean.TRUE` → `Boolean.FALSE`
- **왜 틀렸나**: `if(mvo == null)` 블록은 **실패 케이스** (아이디 못 찾음). 클라이언트한테 success=true로 보내면 프론트가 "성공"으로 인식 → 다음 화면 진행 시도 → 에러.
- **외우는 법**: `if(실패조건)` → success FALSE / `else(정상)` → success TRUE / `catch(예외)` → success FALSE. 마이페이지 회차에서도 같은 실수했음.

### L60: `return _____;` → `return dataVO;`
- **왜 틀렸나**: 미작성. 일찍 종료해서 메소드 끝까지 안 가야 함. 그래야 토큰 생성 안 함.
- **외우는 법**: 실패 case 안에서 `return dataVO;` 박아야 다음 단계로 안 넘어감.

### L63: `MembersVO.getM_pw()` → `membersVO.getM_pw()`
- **왜 틀렸나**: `MembersVO` (대문자 시작) = **클래스명**. `membersVO` (소문자 시작) = **변수명** (위에서 만든 객체). 클래스명으로는 인스턴스 메소드 호출 못 함 — `static`이 아니라.
- **외우는 법**: 점 앞의 첫 글자 보고 판단. 대문자 = 클래스 (static 호출만), 소문자 = 변수/필드 (인스턴스 메소드).

### L66: `return _____;` → `return dataVO;`
- 같은 이유로 미작성. 비밀번호 틀린 경우도 일찍 종료.

### L74-91: 토큰 생성·저장·Map 구성 부분 거의 전부 미작성
- L74: `membersService.deleteRefreshToken(membersVO.getM_id());` — 기존 토큰 삭제
- L77-80: `RefreshTokenVO refreshTokenVO = new RefreshTokenVO();` + setter 3개 + saveRefreshToken
- L83-86: `Map<String,Object> map = new HashMap<>();` + put 3개
- **왜 틀렸나**: 마지막 부분 시간 없거나 모르겠어서 통째 미작성.
- **외우는 법**:
  - **토큰 로테이션 패턴**: 기존 삭제 → 새 VO 생성 → setter로 채우기 → DB 저장
  - **Map 응답 조립 패턴**: `new HashMap<>()` → `put(key, value)` 반복 → `dataVO.setData(map)`

### L91: `dataVO.setData(mvo);` → `dataVO.setData(map);`
- **왜 틀렸나**: Map에 토큰들 담아서 응답해야 함. `mvo`는 그냥 회원 정보 객체. 토큰이 안 들어감.
- **외우는 법**: 응답 데이터 = `dataVO.setData(map)`. mvo는 클라이언트가 이미 알고 있는 본인 정보.

---

## MembersService.java (4/12)

### L7: `MembersVO findById(String getM_idx);` → `MembersVO findById(String id);`
- 변수명 `getM_idx`는 채점 OK (5곳 일관성 룰). 그러나 **`getM_idx`는 변수명으로 부적절** — `getXxx` 는 getter 메소드 관례 이름.
- **외우는 법**: 변수명은 짧고 명확하게. `id`, `userId`, `memberId` 같은 명사형.

### L11-13: `deleteRefreshToken`, `saveRefreshToken` 시그니처 미작성
- 미작성 = 오답. 정답:
  - `void deleteRefreshToken(String id);`
  - `void saveRefreshToken(RefreshTokenVO refreshTokenVO);`
- **외우는 법**: 컨트롤러에서 어떤 메소드 호출하는지 보고 → Service에 시그니처 박기. **컨트롤러 호출 = Service 시그니처** 1:1 대응.

---

## MembersServiceImpl.java (9/25)

### L15-17: `findById` — 정답
사용자 작성:
```java
public MembersVO findById(String getM_idx) {
    return membersMapper.findById(getM_idx);
}
```
일관성 채점 OK. ✅

### L24-32: `deleteRefreshToken`, `saveRefreshToken` 미작성
- 정답:
  ```java
  @Override
  public void deleteRefreshToken(String id) {
      membersMapper.deleteRefreshToken(id);
  }
  @Override
  public void saveRefreshToken(RefreshTokenVO refreshTokenVO) {
      membersMapper.saveRefreshToken(refreshTokenVO);
  }
  ```
- **외우는 법**: ServiceImpl은 거의 다 **Service 시그니처 그대로 + 본문은 mapper 호출 한 줄**. 패턴 외우면 끝.

---

## MembersMapper.java (4/12)

### L15-17: `deleteRefreshToken`, `saveRefreshToken` 시그니처 미작성
- Service와 동일한 시그니처 박으면 됨 (5-파일 일관성).

---

## members-mapper.xml (6/28)

### L8: `m_idx=#{m_idx}` → `m_id=#{m_id}`
- **왜 틀렸나**: DB 컬럼명은 `m_id`. `m_idx`라는 컬럼 존재 X. Java VO `getM_id()` → SQL `m_id`.
- **외우는 법**: VO getter 이름의 카멜케이스 부분 = DB 컬럼명. `getM_id` → `m_id`.

### L11-17: `deleteRefreshToken`, `saveRefreshToken` SQL 미작성
- 정답:
  ```xml
  <delete id="deleteRefreshToken" parameterType="String">
      delete from refresh_tokens where rt_user_id = #{rt_user_id}
  </delete>
  <insert id="saveRefreshToken" parameterType="RefreshTokenVO">
      insert into refresh_tokens (rt_user_id, rt_token)
      values(#{rt_user_id}, #{rt_token})
  </insert>
  ```
- **외우는 법**: SQL 패턴 — `delete from <테이블> where <조건>`, `insert into <테이블> (컬럼들) values(#{값들})`. `#{}` 안은 RefreshTokenVO의 필드명.

---

## 종합 메모

**가장 큰 문제 3가지**:
1. **로그인 흐름의 후반부 (토큰 생성/저장/응답 조립) 미작성** — 빈칸 1/3 가까이가 미작성. 마이페이지 같은 단순 패턴 외엔 아직 손에 안 익음.
2. **`@PostMapping` vs `@GetMapping` 혼동** — POST/GET 선택 기준 명확히 잡고 가야 함.
3. **변수명 충돌** — 파라미터와 내부 변수 같은 이름 박은 거. 컴파일러가 못 찾음.

**다음 회차 목표**: 토큰 로테이션 4단계 (delete → new VO → setters → save) 패턴 외우고 5-파일 묶음 3개 메소드 (`findById`, `deleteRefreshToken`, `saveRefreshToken`) 일관되게 5곳 박는 거.
