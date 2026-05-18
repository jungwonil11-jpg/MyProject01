# 채점 결과 #1 — 2026-05-18 Members 로그인 100% (4회차)

> 이전 회차: practice/2026-05-18_members-login_100/SCORE_1.md (171/191, 89.5%)

## 점수
**190 / 192 (98.96%)**

| 파일 | 점수 |
|---|---|
| MembersController.java (`getLogin`) | 115/116 |
| MembersService.java | 12/12 ✅ 만점 |
| MembersServiceImpl.java | 25/25 ✅ 만점 |
| MembersMapper.java | 12/12 ✅ 만점 |
| members-mapper.xml | 26/27 |

> 회차 3 (89.5%) 대비 **+9.5%p**. Service/Impl/Mapper 4파일 만점. 회차 3에서 정리한 20자리 중 18자리 잡음. 남은 오답 2자리만 정리:

---

## MembersController.java (115/116)

### L71: `dataVO.setData(dataVO);` — 1자리 오답
```java
dataVO.setData(dataVO);
```

**정답**:
```java
dataVO.setData(map);
```

- **왜 틀렸나**: 회차 3에서도 똑같이 틀린 자리. `setData()` 인자에 `dataVO`(자기 자신) 박으면 순환참조. 위에서 만든 `map` 변수가 들어가야 함.
- **외우는 법**: **3-stack 마지막은 setData(map)** — 위에 `Map<String,Object> map = new HashMap<>()` 만들고 3개 `put` 한 그 `map`을 그대로 넣음. `dataVO`는 응답 wrapper지 데이터 자체가 아님.

---

## MembersService.java (12/12) ✅

만점. 시그니처 3개 다 정답:
- `MembersVO findById(String id);`
- `void deleteRefreshToken(String id);`
- `void saveRefreshToken(RefreshTokenVO refreshTokenVO);`

→ **delete/save = void 룰** 완전 정착.

---

## MembersServiceImpl.java (25/25) ✅

만점. 3개 메소드 다 정답:
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

→ **위임 패턴 + void 본문**(return 없음) 완전 정착.

---

## MembersMapper.java (12/12) ✅

만점. Service 시그니처 그대로 복붙 완성.

---

## members-mapper.xml (26/27)

### insert 테이블명 — 1자리 오답
```xml
<insert id="saveRefreshToken" parameterType="RefreshTokenVO">
    insert into members (rt_user_id, rt_token)
    values (#{rt_user_id}, #{rt_token})
</insert>
```

**정답**:
```xml
<insert id="saveRefreshToken" parameterType="RefreshTokenVO">
    insert into refresh_tokens (rt_user_id, rt_token)
    values(#{rt_user_id}, #{rt_token})
</insert>
```

- **왜 틀렸나**: 테이블 `members` → `refresh_tokens`. `members` 테이블엔 `rt_user_id`/`rt_token` 컬럼이 없음 → 런타임에 SQL 에러로 터짐.
- **외우는 법**: **컬럼 prefix가 테이블 가르쳐줌**. `rt_` 접두사 = `refresh_tokens` 테이블. `m_` 접두사 = `members` 테이블. SQL 짤 때 prefix 보고 테이블 결정.
- **회차 3 회귀**: 회차 3엔 delete SQL 테이블이 `members`였는데 회차 4엔 잡음. 근데 이번엔 insert에서 같은 실수. **delete만 안 보고 insert도 같이 봐야 함**.

---

## 종합

**1→2→3→4회차 진척**:
- 1회차 47.0% → 2회차 51.8% → 3회차 89.5% → **4회차 98.96% 🎯**
- Service/Impl/Mapper **3파일 만점** — delete/save = void 룰 완전 정착
- 회차 3에서 정리한 20자리 중 18자리 잡음

**남은 2자리** (다음 회차 만점 노리는 부분):
1. **Controller L71**: `setData(map)` — 회차 3·4 연속 같은 자리 오답. `map` vs `dataVO` 헷갈림
2. **XML insert 테이블**: `refresh_tokens` (members 아님) — `rt_` prefix 보고 테이블 결정

이 2자리만 다음 회차에 잡으면 만점 (192/192).
