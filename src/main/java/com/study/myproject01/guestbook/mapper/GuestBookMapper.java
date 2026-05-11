package com.study.myproject01.guestbook.mapper;

import com.study.myproject01.guestbook.vo.GuestBookVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 방명록 DB 접근을 담당하는 MyBatis 매퍼 인터페이스.
 *
 * <p>{@code @Mapper}를 선언하면 MyBatis가 이 인터페이스의 구현 클래스를 런타임에 자동 생성하고
 * Spring Bean으로 등록한다. 실제 SQL은 {@code resources/mapper/guestbook-mapper.xml}에
 * 작성되어 있으며, XML의 {@code namespace}와 이 인터페이스의 전체 경로가 일치해야 한다.
 *
 * <p>메서드 이름은 mapper XML의 {@code id} 속성과 반드시 동일해야 MyBatis가 연결한다.
 */
@Mapper
public interface GuestBookMapper {

    /**
     * 삭제되지 않은(g_active = 0) 모든 방명록 글 목록을 반환한다.
     *
     * @return 방명록 전체 목록. 데이터가 없으면 빈 List를 반환한다.
     */
    List<GuestBookVO> guestBookList();

    /**
     * 특정 게시글 번호에 해당하는 방명록 글 상세 정보를 반환한다.
     *
     * @param g_idx 조회할 게시글 번호 (PK)
     * @return 해당 게시글 VO. 존재하지 않으면 null을 반환한다.
     */
    GuestBookVO guestBookDetail(String g_idx);

    /**
     * 새 방명록 글을 DB에 삽입한다.
     * 등록 일시(g_regdate)는 SQL의 {@code now()}로 자동 입력된다.
     *
     * @param gvo 삽입할 데이터가 담긴 VO (g_writer, g_subject, g_content, g_email, g_pwd 필수)
     * @return 삽입된 행(row) 수. 성공 시 1, 실패 시 0.
     */
    int guestBookWInsert(GuestBookVO gvo);

    /**
     * 특정 게시글을 논리 삭제한다 (g_active = 1로 업데이트).
     * 물리적으로 행을 삭제하지 않으므로 데이터 복구가 가능하다.
     *
     * @param g_idx 삭제 처리할 게시글 번호 (PK)
     * @return 영향받은 행 수. 성공 시 1, 해당 글이 없으면 0.
     */
    int guestBookDelete(String g_idx);

    /**
     * 특정 게시글의 작성자·제목·이메일·내용을 수정한다.
     *
     * @param gvo 수정할 데이터가 담긴 VO (g_idx 필수, 나머지 필드는 새 값으로 세팅)
     * @return 영향받은 행 수. 성공 시 1, 해당 글이 없으면 0.
     */
    int guestBookUpdate(GuestBookVO gvo);
}
