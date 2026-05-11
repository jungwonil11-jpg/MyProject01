package com.study.myproject01.guestbook.service;

import com.study.myproject01.guestbook.mapper.GuestBookMapper;
import com.study.myproject01.guestbook.vo.GuestBookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@link GuestBookService} 인터페이스의 구현 클래스.
 *
 * <p>{@code @Service}는 이 클래스를 Spring의 서비스 레이어 Bean으로 등록한다.
 * 내부적으로는 {@code @Component}와 동일하게 동작하지만, 역할을 명시적으로 표현하고
 * Spring이 예외 변환(Exception Translation) 같은 추가 기능을 적용할 수 있도록 한다.
 *
 * <p>현재는 비즈니스 로직 없이 매퍼를 위임(delegation)하는 구조이지만,
 * 트랜잭션 처리, 유효성 검증, 파일 업로드 등 부가 로직이 필요해지면 이 클래스에서 구현한다.
 */
@Service
public class GuestBookServiceImpl implements GuestBookService {

    /**
     * MyBatis 매퍼 Bean. DB 쿼리 실행을 담당한다.
     * {@code @Autowired}로 Spring이 자동으로 주입한다.
     * (생성자 주입 방식이 권장되지만 학습 단계에서는 필드 주입도 무방하다.)
     */
    @Autowired
    GuestBookMapper guestBookMapper;

    /**
     * 방명록 전체 목록을 조회하여 반환한다.
     * 매퍼의 guestBookList() 호출 결과를 그대로 반환한다.
     */
    @Override
    public List<GuestBookVO> guestBookList() {
        return guestBookMapper.guestBookList();
    }

    /**
     * 게시글 번호(g_idx)로 특정 게시글 상세를 조회하여 반환한다.
     *
     * @param g_idx 조회할 게시글 번호
     */
    @Override
    public GuestBookVO guestBookDetail(String g_idx) {
        return guestBookMapper.guestBookDetail(g_idx);
    }

    /**
     * 새 게시글을 DB에 삽입한다.
     * 향후 파일 저장 로직(파일명 생성, 디렉터리 저장 등)은 이 메서드에 추가한다.
     *
     * @param gvo 삽입할 게시글 데이터
     * @return 삽입 성공 시 1
     */
    @Override
    public int guestBookWInsert(GuestBookVO gvo) {
        return guestBookMapper.guestBookWInsert(gvo);
    }

    /**
     * 게시글을 논리 삭제한다 (g_active = 1로 변경).
     *
     * @param g_idx 삭제할 게시글 번호
     * @return 처리 성공 시 1
     */
    @Override
    public int guestBookDelete(String g_idx) {
        return guestBookMapper.guestBookDelete(g_idx);
    }

    /**
     * 게시글 내용(작성자, 제목, 이메일, 본문)을 수정한다.
     *
     * @param gvo 수정할 데이터 (g_idx 기준으로 UPDATE)
     * @return 처리 성공 시 1
     */
    @Override
    public int guestBookUpdate(GuestBookVO gvo) {
        return guestBookMapper.guestBookUpdate(gvo);
    }
}
