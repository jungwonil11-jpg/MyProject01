# 채점 결과 #1 — 2026-05-17 MembersVO + RefreshTokenVO 100%

## 점수
**26 / 26 (100%)** — 만점

| 파일 | 점수 |
|---|---|
| MembersVO.java | 18/18 |
| RefreshTokenVO.java | 8/8 |

---

## 메모 — 만점이지만 짚을 부분

### 어노테이션 순서가 강사 코드와 다름

- 강사: `@Data` → `@AllArgsConstructor` → `@NoArgsConstructor`
- 사용자: `@Data` → `@NoArgsConstructor` → `@AllArgsConstructor`

자바 **클래스 레벨 어노테이션은 순서가 동작에 영향을 주지 않음**. 컴파일러/런타임이 같은 위치로 처리. 따라서 O 인정.

"어노테이션 이름·파라미터 = 완전 일치" 룰을 자리별이 아닌 **집합 매칭**으로 해석한 결과:
- 강사 어노테이션 3개 = {@Data, @AllArgsConstructor, @NoArgsConstructor}
- 사용자 어노테이션 3개 = {@Data, @NoArgsConstructor, @AllArgsConstructor}
- 집합 일치 → 동작 동일 → O

### 실무 컨벤션

팀마다 다름. 흔한 순서 2가지:
1. `@Data` → `@NoArgsConstructor` → `@AllArgsConstructor` (사용자 순서) — 기본 생성자 먼저
2. `@Data` → `@AllArgsConstructor` → `@NoArgsConstructor` (강사 순서) — 모든 필드 생성자 먼저

Lombok 동작 자체는 동일. 팀 컨벤션 따라가면 됨.

---

## VO 모드 룰 완화 결과 회고

기본 룰은 클래스 어노테이션 + 필드 선언부를 "빈칸 X"로 분류해서 VO 학습 불가였음. 이번 회차는 VO 모드 한정 완화:
- 클래스 어노테이션 → 빈칸 가능 (3개)
- 필드 타입 `String` → 빈칸 가능 (1개)
- 필드 변수명 → 빈칸 가능, 100% 모드 (14 + 4개)

= 26개 빈칸으로 학습 성립. 룰 결함을 의도로 풀어낸 케이스.
