# 채점 결과 #1 — 2026-05-17 Members 마이페이지 100% (2회차)

> 이전 채점: practice/2026-05-17_members-mypage_100/SCORE_1.md (59/68, 87%)

## 점수
**45 / 68 (66%)**

| 파일 | 점수 |
| --- | --- |
| MembersController.java | 36/50 |
| MembersService.java | 4/4 |
| MembersServiceImpl.java | 5/6 |
| MembersMapper.java | 0/4 |
| members-mapper.xml | 0/4 |

---

## MembersController.java (36/50)

### L102: `GetMapping` → `@GetMapping`
- **왜 틀렸나**: 어노테이션 기호 `@` 빠짐. 단순 오타지만 컴파일 에러 — `@GetMapping`이 어노테이션임을 명시.
- **외우는 법**: Spring 모든 어노테이션은 `@` 필수. IDE 오토컴플릿 쓰면 자동으로 박힘.

### L106: `(MembersVO) SecurityContextHolder...` → `(String) SecurityContextHolder...`
- **왜 틀렸나**: 캐스팅 타입 틀림. SecurityContextHolder에서 꺼낸 principal은 **문자열(userId)**, 회원 객체 아님. `(MembersVO)` 캐스팅하면 ClassCastException 터짐.
- **외우는 법**: SecurityContextHolder는 로그인 사용자의 ID만 가짐. 회원 정보 전체 필요하면 DB 조회(`service.findById(userId)`)로 다시 조회.

### L106: `aaa` → `userId`
- **왜 틀렸나**: 변수명이 의미 없음. 이전 회차에서 강조했듯이 변수명은 타입을 암시. `aaa`는 아무 정보도 안 줌. 100% 모드니까 변수명도 채점 대상 — 일관되게 써야 함. `userId`는 1번만 등장하므로 "사용자가 일관되게 사용" 기준 불만족.
- **외우는 법**: 의미 있는 이름 박을 것. SecurityContext에서 꺼낸 건 `userId` (문자열).

### L108: `findById(aaa.getM_id())` vs `findById(userId)`
- **왜 틀렸나**: 인자가 다름. `aaa.getM_id()`는 메소드 호출인데 aaa가 String이면 getM_id 메소드 없음. 타입 미스매치로 컴파일 안 됨. (위의 L106 타입 오류 때문에 연쇄)
- **외우는 법**: 같은 이유.

### L115: `setData(dataVO)` → `setData(mvo)`
- **왜 틀렸나**: 응답 데이터 자리에 response 객체 자체를 박음. `dataVO.setData(dataVO)`는 "응답 객체 안에 응답 객체 담기" — 무의미. 마이페이지는 조회된 **회원 정보**(`mvo`)를 보내야 클라이언트가 화면 그림.
- **외우는 법**: `setData`엔 조회 결과를 박음. 응답 객체 자체는 X.

### L121: `return mvo` → `return dataVO`
- **왜 틀렸나**: 리턴 타입 미스매치. 메소드 시그니처는 `DataVO getMyPage()` → 리턴 타입 DataVO. 근데 `mvo`는 MembersVO → **컴파일 에러**. 위의 L115 setData 오류와 함께 메소드 전체 구조가 깨짐.
- **외우는 법**: 메소드 시그니처 리턴 타입이랑 return 문 타입 매칭 필수. IDE 빨간줄 뜨면 즉시 고쳐야 함.

---

## MembersService.java (4/4)
오답 없음. `MembersVO findById(String id);` 정확히 채움.

---

## MembersServiceImpl.java (5/6)

### L16: `findById(id)` → `findById(userId)` (인자 이름)
- **왜 틀렸나**: ServiceImpl 구현에선 인자 이름이 `id`인데, Controller에서 호출할 때 `aaa.getM_id()`로 전달. 위의 L108 컴파일 에러 때문에 ServiceImpl 호출도 영향받음.
- **외우는 법**: 같은 이유.

---

## MembersMapper.java (0/4)

### L10: `MembersVO findById(String id);`
- **미작성**: `_____` 그대로였어야 함. 정답 박음.
- **사용자**: 백업본 읽으니까 이미 원본 복구됨. 원래 사용자가 어떻게 했는진 알 수 없지만, 현 상태 채점 기준상 0점.

---

## members-mapper.xml (0/4)

### L7: `<select id="_____">`
- **미작성**: `<select id="findById">` 정답.
- **사용자**: 마찬가지로 원본 복구됨. 0점.

---

## 종합 평가

이번 회차는 **첫 회차 87% → 66%로 하락**. 

4가지 뿌리 문제:
1. **어노테이션 `@` 빠짐** — IDE 오토컴플릿 안 씀 또는 수작업 오타
2. **SecurityContext 캐스팅 타입 틀림** (String → MembersVO) + **변수명 의미 없음**(`aaa`) → 구문 에러로 연쇄 영향
3. **setData 인자 오류** (응답 객체 자체 vs 조회 결과)
4. **return 타입 미스매치** (메소드 시그니처와 불일치)

첫 회차에서는 "5파일 일관성 + null 검사 + setData 인자" 가 주 오류였는데, 이번엔 더 기초적인 부분(캐스팅, 리턴 타입, 어노테이션 기호)까지 꼬임. 

## 조언
- 이전 회차 SCORE를 다시 읽고 같은 실수 반복 안 하기
- 컴파일러 빨간줄 무시 금지 — "컴파일되면 OK" 아님. **컴파일 통과까지**가 기본
- SecurityContextHolder는 문자열(ID)만 줌 — 객체 캐스팅 X
