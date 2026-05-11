package com.study.myproject01.guestbook.service;

import com.study.myproject01.guestbook.vo.GuestBookVO;

import java.util.List;

/**
 * 방명록 비즈니스 로직을 정의하는 서비스 인터페이스.
 *
 * <p>컨트롤러는 이 인터페이스 타입으로 의존성을 주입받아 사용하며,
 * 실제 구현은 {@link GuestBookServiceImpl}이 담당한다.
 *
 * <p>인터페이스를 두는 이유:
 * <ul>
 *   <li>컨트롤러와 구현체 간의 결합도(coupling)를 낮춘다.</li>
 *   <li>Spring AOP(트랜잭션 등)가 JDK 동적 프록시를 생성할 수 있다.</li>
 *   <li>구현체를 교체하거나 Mock 객체로 테스트하기 쉬워진다.</li>
 * </ul>
 */
public interface GuestBookService {

    /**
     * 삭제되지 않은 방명록 글 전체를 조회한다.
     *
     * @return 방명록 목록. 없으면 빈 List.
     */
    List<GuestBookVO> guestBookList();

    /**
     * 특정 게시글 번호로 방명록 글 상세를 조회한다.
     *
     * @param g_idx 조회할 게시글 번호
     * @return 해당 게시글 VO. 없으면 null.
     */
    GuestBookVO guestBookDetail(String g_idx);

    /**
     * 방명록 글을 새로 등록한다.
     *
     * @param gvo 등록할 데이터 VO
     * @return 성공 시 1, 실패 시 0
     */
    int guestBookWInsert(GuestBookVO gvo);

    /**
     * 방명록 글을 논리 삭제한다 (g_active = 1).
     *
     * @param g_idx 삭제할 게시글 번호
     * @return 성공 시 1, 실패 시 0
     */
    int guestBookDelete(String g_idx);

    /**
     * 방명록 글의 내용을 수정한다.
     *
     * @param gvo 수정할 데이터 VO (g_idx 포함)
     * @return 성공 시 1, 실패 시 0
     */
    int guestBookUpdate(GuestBookVO gvo);
}
