# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# 애플리케이션 실행
./gradlew bootRun

# 빌드
./gradlew build

# 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests "com.study.myproject01.MyProject01ApplicationTests"
```

서버 기본 포트: `http://localhost:8080`

## 기술 스택

- **Spring Boot 4.0.6**, Java 21
- **MyBatis 4.0.1** — SQL 매핑 (XML mapper 방식)
- **MySQL** — DB명 `dbstudy`, 사용자 `dbuser`
- **Lombok** — 보일러플레이트 제거

## 패키지 구조

```
com.study.myproject01
└── {도메인}/
    ├── controller/   — @RestController, URL 매핑
    ├── service/      — 비즈니스 로직 (예정)
    ├── mapper/       — MyBatis @Mapper 인터페이스 (예정)
    └── vo/           — Value Object / DTO (예정)
```

MyBatis mapper XML 파일 위치: `src/main/resources/mapper/*.xml`  
VO 클래스는 `type-aliases-package`에 등록되어 XML에서 단순 클래스명으로 사용 가능.

## 프로젝트 코드 컨벤션

### Boolean 래퍼 강제 (`Boolean.TRUE` / `Boolean.FALSE`)

`DataVO.setSuccess()` 등 Boolean 래퍼 타입 setter에는 **무조건 `Boolean.TRUE` / `Boolean.FALSE`** 사용.

```java
❌ dataVO.setSuccess(true);
✅ dataVO.setSuccess(Boolean.TRUE);
```

**적용 시점 (Claude가 코드 생성·복사할 때 자동 적용):**
- 새 학습 템플릿 만들 때 (외부 코드 복사 시 lowercase `true` 묻어오면 즉시 교체)
- 사용자가 직접 손으로 고친 `Boolean.TRUE`를 되돌리지 X

**Why:** DataVO.success 필드가 Boolean 래퍼 타입이라 wrapper로 일관 유지. 사용자가 2026-05-18 채팅에서 통일 결정. 메모리 + CLAUDE.md 이중으로 박혀 있어 템플릿 재생성 시 누락 방지.

### String 빈값 체크 — `.isBlank()` 통일 + 채점 동등 처리

**사용자 풀이는 무조건 `.isBlank()`** 로 통일. 강사 코드/템플릿에 `.isEmpty()` 박혀있어도 그대로 따라하지 X.

```java
✅ if (refreshToken == null || refreshToken.isBlank())
⚠️ if (refreshToken == null || refreshToken.isEmpty())  // 동작은 하지만 사용자 풀이는 isBlank로 통일
```

**채점 룰 (Claude가 SCORE 매길 때 적용):**
- String 변수에 `.isBlank()` vs `.isEmpty()` → **둘 다 정답 처리 ✅** (의미 동일).
- 사용자 풀이 isBlank, 강사 코드 isEmpty → ✅
- 사용자 풀이 isEmpty, 강사 코드 isBlank → ✅
- 단 Collection (List/Map/Set) 에 `.isBlank()` 박은 건 ❌ (컴파일 에러, 메서드 자체 없음).

**Why:** isBlank 는 공백("   ")도 빈 것으로 잡아서 더 안전. 사용자는 본인 풀이 일관성을 위해 isBlank 통일 결정 (2026-05-19 채팅). 채점에서 둘 다 OK 로 처리해야 학습 의지 안 깎임.

### Oracle SQL 병기 — `[ORACLE]` 주석 컨벤션

MySQL과 문법 차이가 있는 쿼리에만 Oracle 버전을 `<!-- [ORACLE] ... -->` 주석으로 병기.
나머지 표준 SQL(SELECT, DELETE, 일반 INSERT)은 차이 없으므로 병기 X.

**현재 적용 위치 (3군데):**

| 파일 | 쿼리 | MySQL | Oracle |
|------|------|-------|--------|
| `members-mapper.xml` | `register` | `m_idx` AUTO_INCREMENT (컬럼 생략) | `seq_members.nextval` 인라인 |
| `members-mapper.xml` | `deleteAccount` | `now()` | `sysdate` |
| `guestbook-mapper.xml` | `guestBookInsert` | `now()` | `SYSDATE` |

**`/연습` blank 룰 — `[ORACLE]` 주석 안:**
- Oracle 특화 토큰만 blank: `nextval`, `SYSDATE`
- 공통 SQL(INSERT 컬럼·값 목록)은 MySQL 쪽과 동일 룰 적용

## 노션 정리 방식
- 노션 상위 페이지: 🌿 Spring Boot (5월~)


## 사용자 컨텍스트

Java 1월부터 시작한 학습자. 현재 Spring Boot(5월~) 단계. 목표는 Spring Boot 백엔드 + React 프론트엔드 풀스택.
개념 설명 시 비유와 예시를 활용하면 이해가 빠름.

## 페르소나

@~/.claude/persona-20대시니컬남성.md
