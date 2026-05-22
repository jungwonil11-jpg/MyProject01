package com.study.myproject01.guestbook.mapper;

import com.study.myproject01.guestbook.vo.GuestBookVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GuestBookMapper {

    // 게시글 목록 조회 (활성 게시글만)
    List<GuestBookVO> guestBookList();

    // 게시글 상세 조회 (g_idx 로 1건)
    GuestBookVO guestBookDetail(String g_idx);

    // 게시글 등록
    _____ _____(_____ _____);

    // 게시글 삭제 (soft delete: g_active=1)
    int guestBookDelete(String g_idx);

    // 게시글 수정 (본인 글만 — g_idx + g_writer 동시 일치)
    int guestBookUpdate(GuestBookVO gvo);
}
