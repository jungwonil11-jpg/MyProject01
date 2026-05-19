# 2026-05-19 Members 토큰 재발급 100% (2회차)

## 회차 정보
- 기능: 토큰 재발급 (refresh)
- 난이도: 100% ([LEARN] 로그 자리 6개 포함)
- 호출: `/도전100 2`
- 시작: 2026-05-19
- 이전 회차: `2026-05-19_members-refresh_100/` (SCORE_1: 155/256, 60.5%)

## 학습 대상 파일
1. `src/.../controller/MembersController.java` — getRefreshToken 빈칸
2. `src/.../service/MembersService.java` — 3개 시그니처 빈칸 (findRefreshToken, deleteRefreshToken, saveRefreshToken)
3. `src/.../service/MembersServiceImpl.java` — 3개 @Override 본문 빈칸
4. `src/.../mapper/MembersMapper.java` — 3개 시그니처 빈칸
5. `src/main/resources/mapper/members-mapper.xml` — 3개 SQL 빈칸

## 진행 방법
IntelliJ에서 5개 파일 열고 `_____` 채우기. 끝나면 `/연습 채점`.

## 1회차 핵심 학습 포인트 (재발견용)
- **흐름**: body 추출 → 빈값 → DB 조회 → JWT 검증 → 새 토큰 → 로테이션 → 응답
- **L141 자리**: `membersService.findRefreshToken(refreshToken)` (필드, 클래스명 X)
- **L152 JWT 검증**: `jwtUtil.validateToken(refreshToken)` (findById 아님)
- **catch 두 개**: ExpiredJwtException (specific) → Exception (general) 순
- **응답 7단계**: map.put 2개 + setSuccess + setMessage + setData
- **만료 처리**: `e.getClaims().getSubject()` 로 userId 추출
- **XML**: id 속성값 = Mapper 메서드명. parameterType = 인자 타입, resultType = 결과 매핑 타입
