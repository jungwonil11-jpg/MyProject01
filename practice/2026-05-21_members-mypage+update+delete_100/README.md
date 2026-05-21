# 2026-05-21 Members/마이페이지+회원정보 수정+회원 탈퇴 100% 회차

5-파일 묶음 **다중 기능 100%** 회차. **RUD 묶음** (Read·Update·Delete) — 사용자 정보 라이프사이클 후반.

## 학습 대상 메서드 (3개)

| 메서드 | URL | 핵심 |
|--------|-----|------|
| `getMyPage` | GET `/members/mypage` | SecurityContext → userId → findById |
| `getUpdate` | POST `/members/update` | SecurityContext userId → mvo.setM_id → update |
| `getDelete` | GET `/members/delete` | refreshToken 삭제 → soft delete (m_active=1) |

## 학습 대상 파일 (5-파일 묶음)

- `src/main/java/com/study/myproject01/members/controller/MembersController.java`
- `src/main/java/com/study/myproject01/members/service/MembersService.java`
- `src/main/java/com/study/myproject01/members/service/MembersServiceImpl.java`
- `src/main/java/com/study/myproject01/members/mapper/MembersMapper.java`
- `src/main/resources/mapper/members-mapper.xml`

## 백업

`.practice-backup/2026-05-21_members-mypage+update+delete_100/` 에 강사 원본 5개 박혀있음. 채점 기준 = 백업.

## 진행

1. IntelliJ에서 위 5개 파일 열고 `_____` 빈칸 채워넣기
2. mypage / update / delete 세 흐름 모두 풀어야 함
3. 막히면 `/힌트`
4. 다 풀면 `/연습 채점`

## 채점 시

다중 기능이라 채점 후 `Members/마이페이지` + `Members/회원정보 수정` + `Members/회원 탈퇴` 세 행 각각 +1, 그리고 `Members/마이페이지+회원정보 수정+회원 탈퇴` 묶음 행도 +1.
