# 2026-05-18 Members 로그인 100% (4회차)

> 이전 회차:
> - `2026-05-17_members-login_100/SCORE_1.md` (90/192, 47.0%)
> - `2026-05-17_members-login_100_2/SCORE_1.md` (99/191, 51.8%)
> - `2026-05-18_members-login_100/SCORE_1.md` (171/191, 89.5%)

## 회차 정보
- 시작: 2026-05-18
- 기능: 로그인 (`POST /members/login`)
- 난이도: **100%**

## 학습 대상 파일
1. `src/main/java/com/study/myproject01/members/controller/MembersController.java` (`getLogin`)
2. `src/main/java/com/study/myproject01/members/service/MembersService.java`
3. `src/main/java/com/study/myproject01/members/service/MembersServiceImpl.java`
4. `src/main/java/com/study/myproject01/members/mapper/MembersMapper.java`
5. `src/main/resources/mapper/members-mapper.xml`

---

## 회차 3에서 틀린 20자리 (이번에 잡을 것)

### Controller `getLogin` (6자리)
| 라인 | 오답 | 정답 |
|---|---|---|
| L31 | `@RequestBody String mvo` | `@RequestBody MembersVO mvo` |
| L35 | `mvo.getM_Id()` | `mvo.getM_id()` |
| L66 | `map.put("userId",userId)` | `map.put("membersVO",membersVO)` |
| L69 | `response.response(_____)` (미작성) | `dataVO.setSuccess(Boolean.TRUE)` |
| L70 | `membersVO.set("로그인 성공")` | `dataVO.setMessage("로그인 성공")` |
| L71 | `dataVO.setData(dataVO)` | `dataVO.setData(map)` |

### Service / Mapper 리턴 타입 (4자리)
delete/save는 **void 고정**. 이름이 리턴타입 결정:

| 위치 | 정답 |
|---|---|
| Service L12 | `void deleteRefreshToken(...)` |
| Service L14 | `void saveRefreshToken(...)` |
| Mapper L15 | `void deleteRefreshToken(...)` |
| Mapper L17 | `void saveRefreshToken(...)` |

### ServiceImpl (3자리)
| 라인 | 정답 |
|---|---|
| L16 | `findById(mvo)` (시그니처 파라미터 그대로 전달, `.getM_Id` 호출 X) |
| L25 | `public void deleteRefreshToken(...)` |
| L30 | `public void saveRefreshToken(...)` |

### XML deleteRefreshToken SQL (3자리)
```xml
delete from refresh_tokens where rt_user_id = #{rt_user_id}
```
- 테이블: `refresh_tokens` (members 아님)
- 컬럼: `rt_user_id` (m_id 아님)
- 메소드명 `deleteRefreshToken` 이 가르쳐줌 — refresh_tokens 테이블에 prefix `rt_`

---

## 만점 전략

1. **Service부터** 풀기 → 리턴타입 `void` 박고 시작
2. ServiceImpl/Mapper는 Service 복붙
3. XML — 메소드명에서 테이블 추론
4. Controller 끝부분 3-stack 손에 익히기:
   ```java
   dataVO.setSuccess(Boolean.TRUE);   ← 또는 true (둘 다 정답)
   dataVO.setMessage("로그인 성공");
   dataVO.setData(map);
   ```

끝나면 `/연습 채점`.
