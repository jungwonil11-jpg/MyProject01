# 2026-05-17 MembersVO + RefreshTokenVO 100%

## 회차 정보
- 시작: 2026-05-17
- 도메인: Members (VO 2개)
- 학습 대상: VO 단일 파일 2개 동시 학습
- 난이도: **100%** + VO 모드 룰 완화 적용

## 학습 대상 파일
1. `src/main/java/com/study/myproject01/members/vo/MembersVO.java`
2. `src/main/java/com/study/myproject01/members/vo/RefreshTokenVO.java`

## VO 모드 빈칸 룰 (스킬 기본 룰에 대한 완화)

기본 룰은 클래스 어노테이션과 필드 선언부를 "빈칸 X" 카테고리로 분류 → VO는 빈칸 풀이 0개라 학습 불가. 이 회차는 룰 완화 적용:

| 자리 | 기본 룰 | 이 회차 (100%) |
|---|---|---|
| 클래스 레벨 어노테이션 (`@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`) | 빈칸 X | **빈칸** |
| 필드 타입 (`String`) | 빈칸 X | **빈칸** |
| 필드 변수명 (`m_idx` 등) | 빈칸 X | **빈칸** (100%만) |
| `private` 접근제어자 | 빈칸 X | 빈칸 X 유지 |
| `import` 구문 | 빈칸 X | 빈칸 X 유지 |
| `public class XXX` 선언 | 빈칸 X | 빈칸 X 유지 |

## 진행 방법
1. IntelliJ에서 2개 파일 열고 `_____` 채움
2. 어노테이션은 `import lombok.*` 를 힌트로 활용 — `Data`, `AllArgsConstructor`, `NoArgsConstructor` 중 매핑
3. 필드 변수명은 강사 코드와 정확히 일치해야 함 (DB 컬럼명과 직결되니까)
4. 끝나면 `/연습 채점`

## 채점 후
- 사용자 작성본은 회차 폴더 안에 보존
- src/ 는 원본으로 자동 복구
- VO 단일 파일 모드는 PROGRESS.md 안 건드림
