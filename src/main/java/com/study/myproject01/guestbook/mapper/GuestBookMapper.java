package com.study.myproject01.guestbook.mapper;

import com.study.myproject01.guestbook.vo.GuestBookVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GuestBookMapper {
    List<GuestBookVO> guestBookList();
    GuestBookVO guestBookDetail(String g_idx);
    int guestBookWInsert(GuestBookVO gvo);
    int guestBookDelete(String g_idx);
    int guestBookUpdate(GuestBookVO gvo);
}
