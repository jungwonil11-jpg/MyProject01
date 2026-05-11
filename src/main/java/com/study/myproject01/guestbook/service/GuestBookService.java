package com.study.myproject01.guestbook.service;

import com.study.myproject01.guestbook.vo.GuestBookVO;

import java.util.List;

public interface GuestBookService {

     //리스트
    List<GuestBookVO> guestBookList();

     //인서트
    int guestBookInsert(GuestBookVO gvo);

    //디테일
    GuestBookVO guestBookDetail(String g_idx);

    //딜레트
    int guestBookDelete(String g_idx);

    //업데이트
    int guestBookUpdate(GuestBookVO gvo);
}
