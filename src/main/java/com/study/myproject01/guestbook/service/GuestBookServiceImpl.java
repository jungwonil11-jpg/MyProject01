package com.study.myproject01.guestbook.service;

import com.study.myproject01.guestbook.mapper.GuestBookMapper;
import com.study.myproject01.guestbook.vo.GuestBookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestBookServiceImpl implements GuestBookService {

    @Autowired
    GuestBookMapper guestBookMapper;

    //리스트
    @Override
    public List<GuestBookVO> guestBookList() {
        return guestBookMapper.guestBookList();
    }

    //인서트
    @Override
    public int guestBookInsert(GuestBookVO gvo) {
        return guestBookMapper.guestBookInsert(gvo);
    }

    //디테일
    @Override
    public GuestBookVO guestBookDetail(String g_idx) {
        return guestBookMapper.guestBookDetail(g_idx);
    }

    //딜레트
    @Override
    public int guestBookDelete(String g_idx) {
        return guestBookMapper.guestBookDelete(g_idx);
    }

    //업데이트
    @Override
    public int guestBookUpdate(GuestBookVO gvo) {
        return guestBookMapper.guestBookUpdate(gvo);
    }
}
