# 채점 결과 #1 — 2026-05-17 Members 로그인 100% (2회차)

> 이전 회차: practice/2026-05-17_members-login_100/SCORE_1.md (90/192, 47.0%)

## 점수
**99 / 191 (51.8%)**

| 파일 | 점수 |
|---|---|
| MembersController.java (`getLogin`) | 99/117 |
| MembersService.java | 0/12 |
| MembersServiceImpl.java | 0/23 |
| MembersMapper.java | 0/12 |
| members-mapper.xml | 0/27 |

> 1회차 (47.0%) 대비 +4.8%p. Controller 본문은 큰 진전 (특히 토큰 로테이션 + Map 조립 패턴 다 잡음). 단 Service~XML 통째 미작성 + Controller 끝부분(catch + 성공 응답) 미작성.

---

## MembersController.java (99/117)

### 잘한 부분 (1회차 대비)

✅ **`@PostMapping`** 박음 (1회차 GetMapping → 수정)
✅ **`MembersVO membersVO`** 별도 변수명 사용 (1회차 mvo 충돌 → 수정)
✅ **`mvo.getM_id()`** 정확 (1회차 getM_idx → 수정)
✅ **`Boolean.FALSE`** 정확 (1회차 TRUE → 수정)
✅ **두 번의 `return dataVO`** 정확
✅ **토큰 로테이션 4단계** 통째 정답 (1회차 미작성 → 완성)
✅ **Map 조립 3개 put** 통째 정답 (1회차 미작성 → 완성)

### L88-90: 성공 응답 부분 — 미작성

```java
dataVO._____(_____);
dataVO._____("로그인 성공");
dataVO._____(_____);
```

**정답**:
```java
dataVO.setSuccess(true);
dataVO.setMessage("로그인 성공");
dataVO.setData(map);
```

- **왜 빠뜨렸나**: Map 조립 끝나고 응답에 넣는 단계인데 시간 부족 or 흐름 끊김
- **외우는 법**: **성공 분기의 3-stack** = `setSuccess(true)` → `setMessage("성공메시지")` → `setData(데이터)`. 데이터는 위에서 만든 Map 변수(`map`).

### L93-99: catch 블록 + 최종 return — 미작성

```java
} _____ (_____ _____) {
    _____._____(_____._____);
    _____._____("서버 오류 : " + _____._____());
}

_____ _____;
```

**정답**:
```java
} catch (Exception e) {
    dataVO.setSuccess(Boolean.FALSE);
    dataVO.setMessage("서버 오류 : " + e.getMessage());
}

return  dataVO;
```

- **왜 빠뜨렸나**: try 블록 본문 풀다가 catch 단계에서 손 놓은 듯
- **외우는 법**:
  - **catch 패턴**: `} catch (Exception e) { ... }` — 예외 타입 + 변수명 `e` 관례
  - **catch 안의 3-stack**: `setSuccess(Boolean.FALSE)` + `setMessage("서버 오류 : " + e.getMessage())` (예외 메시지 첨부)
  - **마지막 return**: 메소드 끝은 항상 `return dataVO;` (try 안에서 일찍 return 안 했으면 여기로 옴)

---

## MembersService.java (0/12)

전체 미작성. 3개 시그니처:

```java
public interface MembersService {
    MembersVO findById(String id);
    void deleteRefreshToken(String id);
    void saveRefreshToken(RefreshTokenVO refreshTokenVO);
    ...
}
```

- **왜 미작성**: Service 파일 자체를 안 봤거나, 빈칸 보고 멘붕
- **외우는 법**:
  - **Service 시그니처 = Controller 호출문에서 추출**:
    - `membersService.findById(...)` → `MembersVO findById(String id)`
    - `membersService.deleteRefreshToken(...)` → `void deleteRefreshToken(String id)` (리턴 없음 → void)
    - `membersService.saveRefreshToken(...)` → `void saveRefreshToken(RefreshTokenVO refreshTokenVO)`
  - **리턴 타입 결정**: Controller에서 결과 받음? → 그 타입. 안 받음? → void.

---

## MembersServiceImpl.java (0/23)

전체 미작성. ServiceImpl 구현은 **거의 다 위임 패턴** — Service 시그니처 그대로 + 본문 한 줄(매퍼 호출):

```java
@Override
public MembersVO findById(String id) {
    return membersMapper.findById(id);
}

@Override
public void deleteRefreshToken(String id) {
    membersMapper.deleteRefreshToken(id);
}

@Override
public void saveRefreshToken(RefreshTokenVO refreshTokenVO) {
    membersMapper.saveRefreshToken(refreshTokenVO);
}
```

- **외우는 법**: ServiceImpl 4-stack 패턴
  1. `@Override`
  2. `public <리턴타입> <메소드명>(<파라미터>)` (Service 시그니처 복붙)
  3. `return membersMapper.<메소드명>(<파라미터>);` (void면 `return` 빼고)
  4. 닫기

→ **타이핑 거의 무뇌**. Service만 알면 자동 생성됨.

---

## MembersMapper.java (0/12)

Service와 동일한 시그니처 박으면 됨:

```java
MembersVO findById(String id);
void deleteRefreshToken(String id);
void saveRefreshToken(RefreshTokenVO refreshTokenVO);
```

- **외우는 법**: Mapper 시그니처 = Service 시그니처 복붙. 5-파일 일관성의 일부.

---

## members-mapper.xml (0/27)

전체 미작성. 3개 SQL:

```xml
<select id="findById" parameterType="String" resultType="MembersVO">
    select * from members where m_active = 0 and m_id=#{m_id}
</select>

<delete id="deleteRefreshToken" parameterType="String">
    delete from refresh_tokens where rt_user_id = #{rt_user_id}
</delete>

<insert id="saveRefreshToken" parameterType="RefreshTokenVO">
    insert into refresh_tokens (rt_user_id, rt_token)
    values(#{rt_user_id}, #{rt_token})
</insert>
```

- **외우는 법**:
  - SQL 3종: SELECT / DELETE / INSERT
  - `#{...}` 안은 **파라미터 객체의 필드명** (deleteRefreshToken 처럼 String 받으면 임의 이름 OK이지만, 관례상 컬럼명 그대로)
  - **id 속성 = 5-파일 메소드명**과 정확히 일치해야 동작

---

## 종합

**1회차 → 2회차 진척**:
- Controller 본문 핵심 (토큰 로테이션, Map 조립) 잡음 — 큰 진전
- **변수명 충돌, getM_id 헷갈림, POST/GET, Boolean.FALSE/TRUE** 1회차 오답 4개 다 수정됨

**남은 약점**:
1. **Controller 끝부분(catch + 성공 응답)** — 7~8개 자리 미작성. 다음 회차에선 끝까지 가기
2. **Service/Impl/Mapper/XML 4파일** — 통째 미작성. **점수 절반 이상이 여기서 깎임**. 다음 회차 우선 학습 대상.

**다음 회차 목표**:
- Controller 끝까지 (성공 응답 3-stack + catch 3-stack + return)
- Service/Impl/Mapper/XML 4파일을 **세트로** 풀기 (한 메소드 = 4파일 5곳 같은 이름 박는 연습)
