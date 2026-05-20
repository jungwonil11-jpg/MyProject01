# 2026-05-19 Members 토큰 재발급 100%

## 회차 정보
- 기능: 토큰 재발급 (refresh)
- 난이도: 100% (빈칸 풀 전부 + [LEARN] 로그 자리 6개 포함)
- 호출: `/도전100 2`
- 시작: 2026-05-19

## 학습 대상 파일
1. `src/main/java/com/study/myproject01/members/controller/MembersController.java` — getRefreshToken 메서드 빈칸
2. `src/main/java/com/study/myproject01/members/service/MembersService.java` — 3개 시그니처 빈칸 (findRefreshToken, deleteRefreshToken, saveRefreshToken)
3. `src/main/java/com/study/myproject01/members/service/MembersServiceImpl.java` — 3개 @Override 본문 빈칸
4. `src/main/java/com/study/myproject01/members/mapper/MembersMapper.java` — 3개 시그니처 빈칸
5. `src/main/resources/mapper/members-mapper.xml` — 3개 SQL 빈칸 (delete/insert/select)

## 진행 방법
IntelliJ에서 5개 파일 열고 `_____` 채우기. [LEARN] 주석 위에 박힌 의도 설명 보면서 로그 라인도 같이 채워야 함. 끝나면 `/연습 채점`.

## 학습 포인트
- **토큰 재발급 흐름**: body 추출 → 빈값 체크 → DB 토큰 조회 → JWT 검증 → 새 토큰 생성 → 로테이션 → 응답
- **보안 분기 로그**: 토큰 누락 / DB 없음(위조 가능성) / JWT 위조 / 만료(정상) / 알 수 없는 예외
- **2개 catch 분리**: `ExpiredJwtException` (만료 — 정상 흐름) vs `Exception` (예측 못 한 예외)
- **5-파일 일관성**: findRefreshToken / deleteRefreshToken / saveRefreshToken 5곳 메소드명 동일
