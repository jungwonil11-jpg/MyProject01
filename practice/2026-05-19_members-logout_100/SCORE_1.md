# 채점 결과 #1 — 2026-05-19 Members 로그아웃 100%

## 점수
**63 / 72 (87.5%)**

| 파일 | 점수 |
|---|---|
| MembersController.java | 36/45 |
| MembersService.java | 4/4 |
| MembersServiceImpl.java | 9/9 |
| MembersMapper.java | 4/4 |
| members-mapper.xml | 10/10 |

---

## MembersController.java (36/45)

### L177: `"logout"` → `"/logout"`
- **왜 틀렸나**: 메서드 레벨 URL 경로에 슬래시 빠뜨림. 클래스 레벨 `@RequestMapping("/members")` + 메서드 레벨 `"logout"` → 최종 `/memberslogout` 으로 매핑됨 (404).
- **외우는 법**: 메서드 레벨 URL은 항상 `/` 로 시작. 다른 logout 외 기능들 다 `/login`, `/myPage`, `/refresh` 처럼 슬래시 박혀있음.

### L185: `"인증됨"` → `"로그아웃 성공"`
- **왜 틀렸나**: 데이터성 문자열 자리. `setSuccess(Boolean.TRUE)` 뒤에 박는 메시지는 사용자에게 보낼 응답 메시지라 "기능명 + 결과" 형식이 표준.
- **외우는 법**: 같은 컨트롤러의 다른 메서드들 보면 `"로그인 성공"`, `"마이페이지 성공"`, `"재발급 성공"` 형식 일관. 로그아웃도 동일.

### L186: `dataVO.setMessage("로그아웃성공")` → `log.info("로그아웃 성공")` (3 오답)
- **왜 틀렸나**: setMessage를 두 번 호출함 (L185, L186). 두 번째 호출이 첫 번째를 덮어쓰므로 "인증됨" 메시지는 사라지고 "로그아웃성공"만 응답에 박힘. 원본은 `setMessage` (응답 메시지) + `log.info` (서버 로그) 분리.
- **외우는 법**: `setMessage` = 응답용 (마지막 호출이 최종 메시지). `log.info` = 서버 로그 (콘솔/로그파일 출력). 역할 다름. 둘 다 박는 게 디버깅 + 사용자 응답 둘 다 챙기는 패턴.
- 추가: `"로그아웃성공"` (사용자) vs `"로그아웃 성공"` (원본) — 띄어쓰기 다름. 데이터성 문자열 자리는 완전 일치 채점.

### L189: `"인증안됨"` → `"로그아웃 실패"`
- 같은 이유 (L185)

### L190: `dataVO.setMessage("로그아웃실패")` → `log.info("로그아웃 실패")` (3 오답)
- 같은 이유 (L186)
