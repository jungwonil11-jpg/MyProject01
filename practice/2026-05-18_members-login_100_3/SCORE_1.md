# 채점 결과 #1 — 2026-05-18 Members 로그인 100%

## 점수
**185 / 190 (97.4%)**

| 파일 | 점수 |
|---|---|
| MembersController.java | 116/116 |
| MembersService.java | 12/12 |
| MembersServiceImpl.java | 23/23 |
| MembersMapper.java | 12/12 |
| members-mapper.xml | 22/27 |

---

## MembersController.java (116/116)

오답 없음.

---

## MembersService.java (12/12)

오답 없음.

---

## MembersServiceImpl.java (23/23)

오답 없음.

---

## MembersMapper.java (12/12)

오답 없음.

---

## members-mapper.xml (22/27)

### L7: `FindById` → `findById`
- **왜 틀렸나**: XML의 `id` 속성값은 Mapper 인터페이스의 메소드명과 정확히 일치해야 MyBatis가 매핑함. 인터페이스에선 `findById`(소문자 f)인데 XML에만 대문자 `F`를 씀 → 5-파일 일관성 깨짐.
- **외우는 법**: XML id = Java 메소드명 복붙. 대소문자 하나라도 다르면 매핑 실패.

### L12: `members` → `refresh_tokens`
- **왜 틀렸나**: `deleteRefreshToken`은 리프레시 토큰을 삭제하는 메소드. 대상 테이블은 `refresh_tokens`인데 `members` 테이블에서 삭제하면 회원 데이터가 날아감.
- **외우는 법**: 메소드명에 `RefreshToken`이 들어있으면 테이블도 `refresh_tokens`.

### L12: `m_id` → `rt_user_id` (WHERE 절 컬럼)
- **왜 틀렸나**: `refresh_tokens` 테이블의 사용자 식별 컬럼은 `rt_user_id`임. `m_id`는 `members` 테이블 컬럼.
- **외우는 법**: 같은 이유 — 테이블이 다르면 컬럼명도 다름.

### L12: `m_id` → `rt_user_id` (#{} 파라미터)
- **왜 틀렸나**: 같은 이유.

### L16: `memberes` → `refresh_tokens`
- **왜 틀렸나**: L12와 같은 이유. `saveRefreshToken`도 `refresh_tokens` 테이블 대상. 추가로 `memberes`는 오타(`members`도 아님).
- **외우는 법**: RefreshToken 관련 메소드 3개(delete/save/find)는 전부 `refresh_tokens` 테이블.
