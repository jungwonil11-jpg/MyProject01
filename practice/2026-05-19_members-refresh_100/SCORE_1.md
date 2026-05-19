# 채점 결과 #1 — 2026-05-19 Members 토큰 재발급 100%

## 점수
**155 / 256 (60.5%)**

| 파일 | 점수 |
|---|---|
| MembersController.java (refresh) | 95/171 |
| MembersService.java | 10/12 |
| MembersServiceImpl.java | 25/28 |
| MembersMapper.java | 11/12 |
| members-mapper.xml | 14/33 |

> 라인 번호 = 사용자 답안 파일 (`practice/2026-05-19_members-refresh_100/src/.../`) 기준.
> 채점 룰: 데이터성 문자열 ("..." 안 내용) 은 의미 동일하면 ✅. 메서드 호출명 / 변수 타입 / 클래스명은 완전 일치.

---

## MembersController.java (95/171)

### L125: `Map<String, Object>` + 변수명 미작성
- 원본: `@RequestBody Map<String, String> body`
- 사용자: `@RequestBody Map<String, Object> _____`
- **왜 틀렸나**: body 의 값은 토큰 문자열이라 제너릭 2번째도 String. 변수명 (body) 도 미작성.
- **외우는 법**: refresh body 는 단순 토큰 1개 = `<String, String>`. 변수명 `body` 관습.

### L129: `RefreshTokenVO.getRT_Token("_____")` → `body.get("refreshToken")`
- **왜 틀렸나**: body 가 `Map<String, String>` 이라 `.get("키")` 로만 꺼냄. VO 정적 메서드 X.
- **외우는 법**: `@RequestBody Map` → 무조건 `body.get("키")`. JSON 키와 정확히 같은 문자열.

### L134/L144/L155: `log.info(...)` → `log.warn(...)` (3곳)
- **왜 틀렸나**: 토큰 없음 / DB 없음(위조) / JWT 검증 실패 = 보안 이벤트 → warn 레벨. info 는 정상 흐름용.
- **외우는 법**: info=정상, warn=이상 신호, error=예외.

### L141: `MembersService._____(_____)` → `membersService.findRefreshToken(refreshToken)`
- **왜 틀렸나**: `MembersService` = 클래스명, `membersService` = 주입된 필드. Spring 빈은 정적 호출 X. 메서드명 + 인자도 미작성.
- **외우는 법**: 클래스명.메서드 = static. 필드.메서드 = 인스턴스. `@Autowired private MembersService membersService` 가 그 필드.

### L142: `if(!refreshTokenVO == _____)` → `if(storeToken == null)`
- **왜 틀렸나**: `!` 가 의미 반대 + null 미작성. `!객체 == null` 은 컴파일 에러.
- **외우는 법**: null 체크 = `if(obj == null)` 또는 `if(obj != null)`. `!obj` 는 boolean 전용.

### L152: `String userId = membersService.findById(MembersVO.getM_id());` → `String userId = jwtUtil.validateToken(refreshToken);`
- **왜 틀렸나**: refresh 4단계 = JWT 서명/만료 검증. `jwtUtil.validateToken(토큰)` 호출. findById 는 회원 정보 조회용, 인자도 m_id 받지 토큰 받지 않음.
- **외우는 법**: refresh 흐름 — (1)body 추출 (2)빈값 (3)**DB 조회** (4)**JWT 검증** (5)새 토큰 (6)로테이션 (7)응답.

### L153: `if(!AccessToken == _____)` → `if(userId == null)`
- **왜 틀렸나**: `AccessToken` 변수 정의 X = 컴파일 에러. 위 라인의 변수가 `userId`.
- **외우는 법**: validateToken 리턴이 String userId. 다음 줄 그거 null 체크.

### L162-163, L168: `userId` 자리에 `membersVO.getM_id()` 박음 (3곳)
- **왜 틀렸나**: refresh 메서드 안엔 `membersVO` 변수 X (login 메서드의 변수). 5단계는 위 4단계에서 받은 `userId` 그대로.
- **외우는 법**: refresh 안엔 `userId` 만 있음. 다른 메서드 변수 못 빌림.

### L170: `MembersVO.set(refreshTokenVO1)` → `membersService.saveRefreshToken(newToken)`
- **왜 틀렸나**: VO 에 `set()` 같은 메서드 없음 (Lombok @Setter 는 필드별 setter 만). 저장은 서비스 호출.
- **외우는 법**: 토큰 저장 6단계 = `service.saveRefreshToken(새VO)`.

### L174-179: map.put / setSuccess / setMessage / setData 통째 미작성 (6 라인)
- **왜 미작성**: 7단계 응답 부분.
- **외우는 법**: `map.put("accessToken", 새access)` + `map.put("refreshToken", 새refresh)` + `setSuccess(true)` + `setMessage(...)` + `setData(map)`.

### L182: 두 번째 인자 (userId) 미작성
- **왜 틀렸나**: SLF4J `log.info(메시지, 인자)` — `{}` 자리에 박힐 인자 빠짐.
- **외우는 법**: `log.info("...{}...", 인자)`.

### L184: `catch (Exception e)` → `catch (ExpiredJwtException e)`
- **왜 틀렸나**: 첫 catch = specific (만료, 정상 분기). 두 번째 = general. 순서 specific → general 필수.
- **외우는 법**: Exception 먼저 박으면 컴파일러 "unreachable code" 에러.

### L188-193: ExpiredJwt catch 본문 통째 미작성 (6 라인)
- **외우는 법**: `String userId = e.getClaims().getSubject()` (만료 토큰에서도 userId 추출 가능) → DB 삭제 → log.info + setSuccess(FALSE) + setMessage("재로그인 안내").

### L194: 두 번째 catch 미작성
- **외우는 법**: `} catch (Exception e) {`.

### L196-198: Exception catch 본문 미작성 (3 라인)
- **외우는 법**: `log.error("메시지", e)` (SLF4J 표준 — 메시지 + 예외 객체 박으면 스택트레이스 출력) + setSuccess/setMessage.

### L200: `return dataVO;` 미작성

---

## MembersService.java (10/12)

### L16: `MembersVO findByRefreshToken(...)` — 리턴 타입 ❌, 메서드명 ✅
- **왜 틀렸나**: 리턴 타입 잘못 (MembersVO ❌, RefreshTokenVO 가 정답 — DB 의 refresh_tokens 테이블 행). 메서드명 `findByRefreshToken` 은 사용자 코드 안에서 일관 사용 (ServiceImpl + Mapper) → 박힌 자리만 ✅.
- **외우는 법**: 리턴 타입 = 가져오는 데이터 타입. 메서드명 자유지만 5곳 일관 필수.

---

## MembersServiceImpl.java (25/28)

### L35: 위 Service 와 동일 — 리턴 타입 ❌, 메서드명 ✅

---

## MembersMapper.java (11/12)

### L19: 위 Service 와 동일 — 리턴 타입 ❌, 메서드명 ✅

---

## members-mapper.xml (14/33)

### L11-13: delete 쿼리
- 태그명 / id / parameterType ✅ (3/3)
- SQL 본문: `delete from refresh_tokens _____ _____ = #{_____}` — delete/from/refresh_tokens ✅, where/rt_user_id/rt_user_id 미작성 ❌ (3/6)
- 닫는 태그 ✅ (1/1)

**외우는 법**: where 절 + 컬럼명 + #{파라미터} 박을 것.

### L15-18: insert 쿼리
- 태그명 / id ✅
- parameterType: `refreshTokenVO` (대소문자만 다름) — 의미 동일 ✅
- SQL 본문 통째 미작성 (0/8)
- 닫는 태그 ✅

**외우는 법**: `insert into refresh_tokens (rt_user_id, rt_token) values(#{rt_user_id}, #{rt_token})`.

### L19-21: select 쿼리 (findRefreshToken)
- 태그명 ✅, id `findByRefreshToken` (5-파일 일관성 ✅)
- parameterType: `RefreshTokenVO` (원본 String) ❌ — refreshToken 받는 거니 String 이 정답
- resultType: `MembersVO` (원본 RefreshTokenVO) ❌ — DB 에서 가져오는 게 refresh_tokens 행
- SQL 본문 미작성 (0/6)
- 닫는 태그 ✅

**외우는 법**: parameterType = SQL 인자 타입, resultType = SQL 결과 매핑 타입. 둘 다 정확히.
