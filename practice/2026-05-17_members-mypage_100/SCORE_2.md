# 채점 결과 #2 — 2026-05-17 Members 마이페이지 100%

> 이전 채점: SCORE_1.md (참고)

## 점수
**73 / 74 (98.6%)**

| 파일 | 점수 |
|---|---|
| MembersController.java (`getMyPage`) | 49/50 |
| MembersService.java (`findById`) | 3/3 |
| MembersServiceImpl.java (`findById`) | 8/8 |
| MembersMapper.java (`findById`) | 3/3 |
| members-mapper.xml (`findById` SQL) | 10/10 |

---

## MembersController.java (49/50)

### L110: `Boolean.TRUE` → `Boolean.FALSE`

```java
if (mvo == null){
    dataVO.setSuccess(Boolean.TRUE);     ← 오답
    dataVO.setMessage("없는 아이디 입니다.");
}
```

- **왜 틀렸나**: `if(mvo == null)` 블록은 **"회원을 못 찾은 실패 케이스"**. 클라이언트한테 "성공"이라고 보내면 프론트가 마이페이지 렌더링 시도함 → 데이터 없어서 깨짐. success 플래그는 **클라이언트가 분기 판단**하는 신호.
- **외우는 법**:
  - `if (실패조건)` 블록 → `success = FALSE`
  - `else (정상 처리)` 블록 → `success = TRUE`
  - `catch (예외)` 블록 → `success = FALSE`
  → 성공은 정상 경로 1곳, 나머지 다 실패. 같은 메소드 다른 블록끼리 비교해서 일관성 체크하면 안 헷갈림.

---

## MembersService.java (3/3)

오답 없음. `MembersVO findById(String id);` 시그니처 5-파일 일관성 OK.

---

## MembersServiceImpl.java (8/8)

오답 없음. `@Override public MembersVO findById(String id) { return membersMapper.findById(id); }` 정확.

---

## MembersMapper.java (3/3)

오답 없음.

---

## members-mapper.xml (10/10)

오답 없음. SQL `select * from members where m_active = 0 and m_id=#{m_id}` 정확.

---

## 종합

5-파일 묶음 일관성 완벽 (`findById` 5곳 동일). 100% 난이도 첫 회차에서 50개 자리 중 49개 맞음 — 거의 만점. 단 하나 놓친 게 **Boolean.FALSE vs TRUE 의미 구분**. 이건 비즈니스 로직 이해 문제지 문법 문제 아님.
