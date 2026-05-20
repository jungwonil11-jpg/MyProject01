# 학습 제외 메소드

`/연습` 빈칸 처리 시 이 목록의 메소드는 **통째로 삭제** 후 진행.
강사 원본에 있지만 학습 가치 없는 더미(hello, hi 같은 거)를 제외하는 용도.

**적용 시점**: 빈칸 템플릿 생성 시 (`practice/_templates/<기능>_100/` 만들 때).
**적용 X**: 강사 원본 백업(`.practice-backup/`)은 그대로. 채점은 원본 기준.

---

## src/main/java/com/study/myproject01/members/controller/MembersController.java

- getHello
- getHi
- getHello2
- getHi2

## src/main/java/com/study/myproject01/guestbook/controller/GuestBookController.java

(현재 제외 메소드 없음)

---

## 사용법

1. 강사 원본에 더미 메소드 있으면 이 파일에 파일 경로 단위로 추가
2. `/연습 템플릿 재생성 <기능>` 또는 `/연습 템플릿 재생성 all` 실행
3. 다음 `/연습 <기능> 100` 호출부터 자동 제외 반영

## 주의

- **학습 대상 메소드는 절대 박지 말 것**. 학습 대상 메소드 박으면 빈칸이 통째로 사라져 학습 불가능.
- 파일 경로 기준. 같은 메소드명이 다른 파일에 있어도 영향 X (파일 단위 매칭).
- 메소드명만 박음. 시그니처(파라미터 타입 등) 무시 — 같은 이름 메소드는 모두 제거.
