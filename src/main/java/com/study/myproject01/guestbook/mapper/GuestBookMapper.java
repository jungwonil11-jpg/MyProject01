package com.study.myproject01.guestbook.mapper;

import com.study.myproject01.guestbook.vo.GuestBookVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GuestBookMapper {

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
