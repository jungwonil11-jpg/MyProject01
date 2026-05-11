package com.study.myproject01.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 모든 REST API 응답에 사용되는 공통 응답 래퍼(Wrapper) VO.
 *
 * <p>컨트롤러에서 처리 결과를 클라이언트에 반환할 때 이 클래스 하나로 성공/실패 여부,
 * 메시지, 실제 데이터를 묶어서 전달한다. JSON 직렬화 시 아래 구조로 출력된다.
 * <pre>
 * {
 *   "success": true,
 *   "message": "데이터 불러오기 성공",
 *   "data": { ... }
 * }
 * </pre>
 *
 * <p>Lombok 어노테이션 설명:
 * <ul>
 *   <li>{@code @Data} — getter, setter, toString, equals, hashCode 자동 생성</li>
 *   <li>{@code @AllArgsConstructor} — 모든 필드를 받는 생성자 자동 생성</li>
 *   <li>{@code @NoArgsConstructor} — 기본 생성자(인수 없음) 자동 생성.
 *       Jackson이 JSON → 객체 역직렬화 시 기본 생성자를 필요로 하므로 반드시 존재해야 한다.</li>
 * </ul>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataVO {

    /**
     * API 처리 성공 여부.
     * true = 정상 처리, false = 오류 발생
     */
    private boolean success;

    /**
     * 클라이언트에게 전달할 안내 메시지.
     * 성공 시 "등록 성공", 실패 시 예외 메시지 또는 사용자 정의 문구를 담는다.
     */
    private String message;

    /**
     * 실제 응답 데이터.
     * 단일 객체(GuestBookVO), 컬렉션(List&lt;GuestBookVO&gt;) 등 어떤 타입도 담을 수 있도록 Object로 선언.
     * 데이터가 없는 경우(삽입·삭제 결과 등) null로 유지한다.
     */
    private Object data;
}
