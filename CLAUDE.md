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
