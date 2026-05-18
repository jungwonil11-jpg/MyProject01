# 2026-05-17 Members 로그인 100%

## 회차 정보
- 시작: 2026-05-17
- 도메인: Members
- 기능: 로그인 (`POST /members/login`)
- 난이도: **100%** — 구두점/괄호/`@`/`;`/`.`/`=` + 데이터성 문자열 빼고 전부 빈칸

## 학습 대상 파일

1. `src/main/java/com/study/myproject01/members/controller/MembersController.java` (`getLogin` 메소드)
2. `src/main/java/com/study/myproject01/members/service/MembersService.java`
3. `src/main/java/com/study/myproject01/members/service/MembersServiceImpl.java`
4. `src/main/java/com/study/myproject01/members/mapper/MembersMapper.java`
5. `src/main/resources/mapper/members-mapper.xml`

## 기능: 로그인

- **URL**: POST `/members/login`
- **요청 body**: `{ "m_id": "유저ID", "m_pw": "비밀번호" }` (MembersVO 형식)
- **응답**: DataVO { success, message, data: { accessToken, refreshToken, membersVO } }

## 로그인 흐름

1. **아이디 존재 확인**: `membersService.findById(mvo.getM_id())` → null이면 실패
2. **비밀번호 검증**: `passwordEncoder.matches(입력비번, DB비번)` → 불일치면 실패
3. **JWT 토큰 생성**: accessToken + refreshToken (둘 다 m_id로)
4. **기존 refreshToken 삭제**: 중복 로그인 방지 (한 유저는 항상 최신 토큰 1개만)
5. **새 refreshToken DB 저장**: refresh_tokens 테이블에 insert
6. **응답 조립**: Map에 accessToken/refreshToken/membersVO 담아 dataVO.data로

## 사용하는 5-파일 묶음 메소드

| 메소드 | Service 시그니처 | XML SQL |
|---|---|---|
| `findById` | `MembersVO findById(String id)` | `select * from members where m_active=0 and m_id=...` |
| `deleteRefreshToken` | `void deleteRefreshToken(String id)` | `delete from refresh_tokens where rt_user_id=...` |
| `saveRefreshToken` | `void saveRefreshToken(RefreshTokenVO ...)` | `insert into refresh_tokens (...) values(...)` |

→ 5곳 모두 일관된 이름으로 박아야 동작. 강사 이름과 달라도 OK (메소드 호출명/Service 시그니처/ServiceImpl @Override/Mapper 시그니처/XML id 5곳 동일하면).

## 빈칸 X (유지)

- 구두점/괄호/`@`/`;`/`.`/`=`
- 패키지·import·클래스 선언·필드 선언
- **데이터성 문자열 리터럴**: `"없는 아이디 입니다"`, `"비밀번호가 틀렸습니다."`, `"로그인 성공"`, `"서버 오류 : "` (사용자 메시지)
- 다른 기능 메소드 (마이페이지, 토큰 재발급)

> ⚠️ 어노테이션 경로 `"/login"`, map.put 키 `"accessToken"`/`"refreshToken"`/`"membersVO"` 는 식별자 → 빈칸 처리

## 진행 방법

1. IntelliJ에서 5개 파일 열기
2. 컨트롤러의 `getLogin` 메소드만 빈칸 (다른 메소드는 그대로)
3. Service/ServiceImpl/Mapper: 로그인이 쓰는 3개 메소드 (`findById`, `deleteRefreshToken`, `saveRefreshToken`)
4. XML: 위 3개 SQL
5. 끝나면 `/연습 채점`

## 팁

- **BCrypt**: `passwordEncoder.matches(평문, 해시)` — 둘 다 String. 순서 주의 (평문이 먼저).
- **JWT 토큰 생성**: `jwtUtil.generateAccessToken(userId)`, `jwtUtil.generateRefreshToken(userId)` — userId는 String.
- **RefreshTokenVO**: setter `setRt_user_id(...)`, `setRt_token(...)` — 컬럼명 그대로 (Lombok 자동 생성).
- **Map<String, Object>**: accessToken과 refreshToken은 String, membersVO는 객체라 value 타입은 Object로 받아야 다 들어감.
- **DataVO.setSuccess**: `Boolean.FALSE` vs `true` 섞여 있음 — 의미만 같으면 채점 O.
