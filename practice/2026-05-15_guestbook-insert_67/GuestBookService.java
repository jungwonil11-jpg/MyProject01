package com.study.myproject01.guestbook.service;

import com.study.myproject01.guestbook.vo.GuestBookVO;

import java.util.List;

public interface GuestBookService {
    List<GuestBookVO> guestBookList();
    GuestBookVO guestBookDetail(String g_idx);
    _____ _____(_____ gvo);
    int guestBookDelete(String g_idx);
    int guestBookUpdate(GuestBookVO gvo);
}
