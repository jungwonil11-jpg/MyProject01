package com.study.myproject01.guestbook.service;

import com.study.myproject01.guestbook.vo.GuestBookVO;

import java.util.List;

public interface GuestBookService {
    List<GuestBookVO> guestBookList();
    GuestBookVO guestBookDetail(String g_idx);
    int guestBookWInsert(GuestBookVO gvo);
    int guestBookDelete(String g_idx);
    int guestBookUpdate(GuestBookVO gvo);
}
