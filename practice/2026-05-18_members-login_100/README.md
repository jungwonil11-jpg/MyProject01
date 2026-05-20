# 2026-05-18 Members 로그인 100% (3회차)

> 이전 회차:
> - `2026-05-17_members-login_100/SCORE_1.md` (90/192, 47.0%)
> - `2026-05-17_members-login_100_2/SCORE_1.md` (99/191, 51.8%)

## 회차 정보
- 시작: 2026-05-18
- 도메인: Members
- 기능: 로그인 (`POST /members/login`)
- 난이도: **100%**

## 학습 대상 파일

1. `src/main/java/com/study/myproject01/members/controller/MembersController.java` (`getLogin` 메소드)
2. `src/main/java/com/study/myproject01/members/service/MembersService.java`
3. `src/main/java/com/study/myproject01/members/service/MembersServiceImpl.java`
4. `src/main/java/com/study/myproject01/members/mapper/MembersMapper.java`
5. `src/main/resources/mapper/members-mapper.xml`

## 이번 회차 목표

### 2회차에서 풀었던 거 (유지)
- ✅ `@PostMapping("/login")` + `@RequestBody MembersVO mvo`
- ✅ `MembersVO membersVO` (변수명 충돌 방지)
- ✅ `mvo.getM_id()` (m_idx 아님)
- ✅ `Boolean.FALSE` (실패 케이스)
- ✅ 토큰 로테이션 4단계
- ✅ Map 응답 조립

### 2회차에서 못 푼 거 (이번에 잡기)

**Controller 끝부분 (성공 응답 + catch + return)**:
```java
// 성공 응답 3-stack
dataVO.setSuccess(true);
dataVO.setMessage("로그인 성공");
dataVO.setData(map);

} catch (Exception e) {
    dataVO.setSuccess(Boolean.FALSE);
    dataVO.setMessage("서버 오류 : " + e.getMessage());
}

return  dataVO;
```

**Service/ServiceImpl/Mapper/XML 4파일 — 통째 잡기**:

#### Service 시그니처 3개 (간단)
```java
MembersVO findById(String id);
void deleteRefreshToken(String id);
void saveRefreshToken(RefreshTokenVO refreshTokenVO);
```

#### ServiceImpl 패턴 — `@Override + Service 시그니처 + mapper 호출`
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

#### Mapper = Service 복붙
```java
MembersVO findById(String id);
void deleteRefreshToken(String id);
void saveRefreshToken(RefreshTokenVO refreshTokenVO);
```

#### XML SQL 3개
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

## 5-파일 일관성 체크 — `findById` 5곳 동일

| 위치 | 코드 |
|---|---|
| Controller | `membersService.findById(...)` |
| Service | `MembersVO findById(String id);` |
| ServiceImpl | `@Override public MembersVO findById(...)` |
| Mapper | `MembersVO findById(String id);` |
| XML | `<select id="findById" ...>` |

→ 5곳 다 `findById` 박아야 동작. 다른 이름으로 통일해도 OK (단 5곳 일치).

## 진행 방법

1. IntelliJ에서 5개 파일 다 열기
2. Controller → Service → Impl → Mapper → XML 순서로 풀기 (의존 관계 따라)
3. 끝나면 `/연습 채점`

## 팁

- **Controller 본문 끝**: try 블록 안 마지막 = 성공 응답 3-stack, } catch = 예외 처리 3-stack, } 닫고 return
- **Service ↔ ServiceImpl**: 시그니처는 동일, ServiceImpl은 `public` + `@Override` 추가 + 본문 한 줄
- **XML id ↔ Mapper 메소드명**: 정확히 일치해야 MyBatis 매핑 동작
