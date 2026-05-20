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

## 현재 구현된 엔드포인트

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/Members/Hello` | "Hello World" 반환 |
| POST | `/Members/hi` | "Hi World" 반환 |

POST 엔드포인트는 브라우저 직접 접근 불가 — Postman 또는 Thunder Client(VS Code 확장)로 테스트.

## 프로젝트 코드 컨벤션

### Boolean 래퍼 강제 (`Boolean.TRUE` / `Boolean.FALSE`)

`DataVO.setSuccess()` 등 Boolean 래퍼 타입 setter에는 **무조건 `Boolean.TRUE` / `Boolean.FALSE`** 사용.

```java
❌ dataVO.setSuccess(true);
✅ dataVO.setSuccess(Boolean.TRUE);
```

**적용 시점 (Claude가 코드 생성·복사할 때 자동 적용):**
- 새 학습 템플릿 만들 때 (강사 코드 베껴오면 lowercase `true`가 묻어오므로 즉시 교체)
- `/강사싱크` 직후 study 쪽 작업에 진입할 때 (강사 원본이 `true` / `Boolean.TRUE` 섞어 씀 — study에서 통일)
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

## /강사싱크 실행 전 필수 확인 (경고)

**강사 코드는 이미 종료됨 (2026-05-20 기준).** `/강사싱크`를 실행하면 upstream 최신 코드가 main에 들어오고 study에 머지됨.

### 실행 전 Claude가 반드시 경고할 것

> "강사 코드가 종료된 상태입니다. `/강사싱크`를 실행하면 아래 보호 대상 코드와 충돌이 발생할 수 있습니다. 계속 진행하시겠습니까?"

### 보호 대상 — study에서 직접 추가한 코드 (강사 코드에 없음)

| 파일 | 보호 내용 |
|------|-----------|
| `members/controller/MembersController.java` | `getRegister()` 메서드 |
| `members/service/MembersService.java` | `register(MembersVO mvo)` |
| `members/service/MembersServiceImpl.java` | `register()` 구현 |
| `members/mapper/MembersMapper.java` | `register(MembersVO mvo)` |
| `resources/mapper/members-mapper.xml` | `<insert id="register">` 쿼리 |

### 머지 충돌 발생 시 규칙

- 위 보호 대상 코드는 **무조건 살린다** (ours 우선)
- 강사 코드 변경분은 충돌 마커에서 확인 후 수동 판단

## 브랜치 전략

| 브랜치 | 역할 | 규칙 |
|--------|------|------|
| `main` | 강사 코드 미러 | 자습 작업 금지. 강사 싱크 전용. |
| `study` | 자습 작업 공간 | 모든 자습·실습·주석 여기에. |

- 매일 작업은 **`study` 브랜치**에서만 함
- `.claude/`, `CLAUDE.md`, `CLAUDE.local.md` 는 study에만 존재 (main에 commit 금지)
- 싱크 흐름: `/강사싱크` → main에 강사 코드 → study에 머지

upstream: https://github.com/nohssam/2026-springboot01.git
my-branch: study
integration-branch: main

## 노션 정리 방식
- 노션 상위 페이지: 🌿 Spring Boot (5월~)


## 사용자 컨텍스트

Java 1월부터 시작한 학습자. 현재 Spring Boot(5월~) 단계. 목표는 Spring Boot 백엔드 + React 프론트엔드 풀스택.
개념 설명 시 비유와 예시를 활용하면 이해가 빠름.

## 페르소나

@~/.claude/persona-20대시니컬남성.md
