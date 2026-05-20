# 채점 결과 #1 — 2026-05-18 Members 로그인 100% (3회차) — 재채점 v2

> 이전 회차: practice/2026-05-17_members-login_100_2/SCORE_1.md (99/191, 51.8%)

> 📝 **재채점 v2 이유**: v1에서도 룰 위반. 파라미터명을 백업 정답(`id`)과 다르다고 깎았는데, 룰은 **사용자 5곳 일관성**이 기준. 사용자가 Service/Impl/Mapper 3곳에서 `mvo`/`m_id`/`refreshTokenVO`로 일관되게 박았으므로 인정.

## 점수
**171 / 191 (89.5%)**

| 파일 | 점수 |
|---|---|
| MembersController.java (`getLogin`) | 105/115 |
| MembersService.java | 10/12 |
| MembersServiceImpl.java | 22/25 |
| MembersMapper.java | 10/12 |
| members-mapper.xml | 24/27 |

> 2회차 (51.8%) 대비 **+34.1%p 큰 진전**. Service/Impl/Mapper/XML 통째 잡았고 (회차 2엔 0점이었음), Controller 끝부분 catch도 잡음. 남은 약점: 파라미터명 `id` vs `mvo/m_id` 일관성 어긋남, Service 리턴타입 `void` 미적용, Controller 성공 응답 3-stack 직전에 풀이 중단.

---

## MembersController.java (105/115)

### L31: `@RequestBody String mvo` — 1자리 오답
```java
public DataVO getLogin(@RequestBody String mvo){
```

**정답**:
```java
public DataVO getLogin(@RequestBody MembersVO mvo){
```

- **왜 틀렸나**: `@RequestBody`는 JSON 본문을 객체로 역직렬화. `String`이면 m_id/m_pw를 꺼낼 수 없음. 회차 2에선 맞았는데 이번에 회귀.
- **외우는 법**: **@RequestBody = VO 타입 고정**. 단순 값 받을 땐 @RequestParam.

### L35: `mvo.getM_Id()` — 1자리 오답 (대소문자)
```java
MembersVO membersVO = membersService.findById(mvo.getM_Id());
```

**정답**:
```java
MembersVO membersVO = membersService.findById(mvo.getM_id());
```

- **왜 틀렸나**: 컬럼은 `m_id`라서 getter도 `getM_id()`. `getM_Id()`라는 메소드는 Lombok이 안 만들어줌.
- **외우는 법**: getter 이름 = `get` + 필드명 camelCase. 필드가 `m_id`면 `getM_id()`.

### L66: `map.put("userId",userId)` — 2자리 오답
```java
map.put("userId",userId);
```

**정답**:
```java
map.put("membersVO",membersVO);
```

- **왜 틀렸나**: `userId` 변수가 정의되지 않음 (메소드 내에 없음). 클라이언트에게 사용자 정보 통째로 보내야 하므로 `membersVO` 객체를 그대로 박아야 함.

### L69: 미작성 — 3자리 오답
```java
response.response(_____);
```

**정답**:
```java
dataVO.setSuccess(true);
```

또는 (의미 동등 인정):
```java
dataVO.setSuccess(Boolean.TRUE);
```

- **왜 틀렸나**:
  - `response`라는 변수 존재 안 함 (Controller에 필드로 없음) — `dataVO`여야
  - `response()` 메소드도 존재 X — `setSuccess()` 여야
  - 인자 자리 `_____` 그대로 미작성. **`true` 또는 `Boolean.TRUE` 둘 다 정답**. 채점 룰에 `Boolean.*` ↔ primitive 의미 동등이라 명시됨.
- **헷갈리는 이유**: 강사 코드가 같은 메소드 안에서 **성공 케이스는 `true`** (L89), **실패 케이스는 `Boolean.FALSE`** (L94) 로 섞어 씀. 일관성 X. 외우기 어려움.
- **외우는 법**:
  - 둘 다 정답이라 아무거나 박으면 됨 — 막혔을 때 비워두지 말고 일단 박을 것
  - **성공 응답 3-stack**: `dataVO.setSuccess(true)` → `dataVO.setMessage("...")` → `dataVO.setData(...)`. 회차 2부터 계속 빠지는 부분이라 손으로 5번 더 써보기.

### L70: `membersVO.set("로그인 성공")` — 2자리 오답
```java
membersVO.set("로그인 성공");
```

**정답**:
```java
dataVO.setMessage("로그인 성공");
```

- **왜 틀렸나**: 응답 메시지는 `dataVO`에 박음 (`membersVO`는 회원 정보). `set()` 단독 메소드 없음.

### L71: `dataVO.setData(dataVO)` — 1자리 오답
```java
dataVO.setData(dataVO);
```

**정답**:
```java
dataVO.setData(map);
```

- **왜 틀렸나**: `dataVO`를 자기 자신 필드에 박으면 순환참조. 위에서 만든 응답 `map`을 박아야 함.

### 잘 푼 부분 (회차 2 대비 진전)

✅ **catch 블록 + 마지막 return 3-stack 완성** — 회차 2엔 미작성이었던 부분 다 채움:
```java
} catch (Exception e) {
    dataVO.setSuccess(Boolean.FALSE);
    dataVO.setMessage("서버 오류 : " + e.getMessage());
}

return  dataVO;
```

---

## MembersService.java (10/12)

### L12: 리턴 타입 `MembersVO` — 1자리 오답
```java
MembersVO deleteRefreshToken(String m_id);
```

**정답**:
```java
void deleteRefreshToken(String id);
```

- **왜 틀렸나**: `delete`는 DB만 지우면 끝이라 리턴할 게 없음 → `void`. 회차 2 SCORE의 핵심 패턴. 파라미터명 `m_id`는 Impl/Mapper에서 일관되게 썼으므로 인정.
- **외우는 법**: **메소드 이름이 리턴타입을 결정**. `find~` → 데이터 타입, `delete/save` → `void`, `count~` → `int`.

### L14: 리턴 타입 `RefreshTokenVO` — 1자리 오답
```java
RefreshTokenVO saveRefreshToken(RefreshTokenVO refreshTokenVO);
```

**정답**:
```java
void saveRefreshToken(RefreshTokenVO refreshTokenVO);
```

- **왜 틀렸나**: 위와 동일. `save`도 저장만 하고 끝 → `void`.

### 파라미터명 `mvo`/`m_id` — 인정 (5곳 일관성)
L8 `findById(String mvo)`, L12 `deleteRefreshToken(String m_id)` 의 파라미터명은 ServiceImpl/Mapper 3곳에서 일관되게 사용 → 인정. 다만 관례상 단일 String 파라미터는 의미를 그대로 (`id`) 박는 게 가독성 좋음.

---

## MembersServiceImpl.java (22/25)

### L16: mapper 인자 `mvo.getM_Id` — 1자리 오답
```java
return membersMapper.findById(mvo.getM_Id);
```

**정답**:
```java
return membersMapper.findById(id);
```

- **왜 틀렸나**: 파라미터 `mvo`가 `String`인데 `.getM_Id` 호출 + 괄호도 없음 = 컴파일 에러. 빈칸 자리에는 시그니처 파라미터 그대로 (`mvo`) 박으면 OK였음.
- **외우는 법**: ServiceImpl 위임 패턴은 **시그니처 파라미터 그대로 mapper에 전달**. 가공 X.

### L25: deleteRefreshToken 리턴 타입 — 1자리 오답
```java
public MembersVO deleteRefreshToken(String m_id) {
```

**정답**:
```java
public void deleteRefreshToken(String id) {
```

- 리턴 타입만 X (`MembersVO` → `void`). 파라미터명 `m_id`는 일관성 OK.

### L26: 본문 `return` 박음
```java
return membersMapper.deleteRefreshToken(m_id);
```

**정답**:
```java
membersMapper.deleteRefreshToken(id);
```

- 빈칸 자리 토큰은 다 OK. 다만 시그니처가 `void`여야 하는데 사용자는 `MembersVO` 박았기에 `return` 박은 게 일관성 OK. 점수엔 영향 없음.

### L30: saveRefreshToken 리턴 타입 — 1자리 오답
```java
public RefreshTokenVO saveRefreshToken(RefreshTokenVO refreshTokenVO) {
```

**정답**:
```java
public void saveRefreshToken(RefreshTokenVO refreshTokenVO) {
```

- 리턴 타입만 X.

---

## MembersMapper.java (10/12)

### L15: deleteRefreshToken 리턴 타입 — 1자리 오답
```java
MembersVO deleteRefreshToken(String m_id);
```

**정답**:
```java
void deleteRefreshToken(String id);
```

### L17: saveRefreshToken 리턴 타입 — 1자리 오답
```java
RefreshTokenVO saveRefreshToken(RefreshTokenVO refreshTokenVO);
```

**정답**:
```java
void saveRefreshToken(RefreshTokenVO refreshTokenVO);
```

- **Mapper 룰**: Service 시그니처 그대로 복붙. Service에서 `void`로 박으면 Mapper도 `void`. 파라미터명은 5곳 일관성으로 인정.

---

## members-mapper.xml (24/27)

### findById — 만점 (11/11)
사용자 답안 완벽:
```xml
<select id="findById" parameterType="String" resultType="MembersVO">
    select * from members where m_active = 0 and m_id=#{m_id}
</select>
```

### deleteRefreshToken — 3자리 오답 (4/7)
```xml
<delete id="deleteRefreshToken" parameterType="String">
    delete from members where m_id = #{m_id}
</delete>
```

**정답**:
```xml
<delete id="deleteRefreshToken" parameterType="String">
    delete from refresh_tokens where rt_user_id = #{rt_user_id}
</delete>
```

- **왜 틀렸나**: 테이블/컬럼이 잘못됨.
  - 테이블: `members` → `refresh_tokens` (이 메소드는 토큰을 지우는 거지 회원을 지우는 게 아님)
  - 컬럼: `m_id` → `rt_user_id` (refresh_tokens 테이블의 컬럼명)
- **외우는 법**: **SQL 테이블은 메소드 이름이 가르쳐줌**. `deleteRefreshToken` → 지울 대상은 `refresh_tokens`. 컬럼 prefix도 다름 (refresh_tokens는 `rt_`).

### saveRefreshToken — 만점 (9/9)
사용자 답안 완벽 (공백 차이는 무시):
```xml
<insert id="saveRefreshToken" parameterType="RefreshTokenVO">
    insert into refresh_tokens (rt_user_id, rt_token)
    values (#{rt_user_id}, #{rt_token})
</insert>
```

---

## 종합

**1회차 → 2회차 → 3회차 진척**:
- 1회차 47.0% → 2회차 51.8% → **3회차 89.5% 🎯 +37.7%p**
- Service/Impl/Mapper/XML 통째 잡음 — 회차 2의 가장 큰 약점이었던 4파일 해결
- catch 블록도 잡음
- 파라미터명은 일관성 룰에 따라 인정

**남은 약점 (실제 오답 자리만)**:
1. **Controller L31**: `@RequestBody String mvo` → `MembersVO` (회차 2에서 잡았던 거 회귀)
2. **Controller L35**: `getM_Id` → `getM_id` (대소문자)
3. **Controller L66**: `map.put("userId",userId)` → `map.put("membersVO",membersVO)` (`userId` 변수 미정의)
4. **Controller L69-71**: 성공 응답 3-stack 완전 망가짐. 정답:
   ```java
   dataVO.setSuccess(true);
   dataVO.setMessage("로그인 성공");
   dataVO.setData(map);
   ```
5. **Service/Mapper 리턴 타입 4자리**: `delete`/`save` = `void` (현재 `MembersVO`/`RefreshTokenVO`)
6. **ServiceImpl L16**: `mvo.getM_Id` → `mvo` (시그니처 파라미터 그대로)
7. **XML deleteRefreshToken SQL**: `delete from members where m_id` → `delete from refresh_tokens where rt_user_id`

총 20자리 오답. 다음 회차 만점 노리려면 이 20자리만 고치면 됨.
