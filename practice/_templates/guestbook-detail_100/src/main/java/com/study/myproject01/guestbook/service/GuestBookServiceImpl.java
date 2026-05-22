package com.study.myproject01.guestbook.service;

import com.study.myproject01.guestbook.mapper.GuestBookMapper;
import com.study.myproject01.guestbook.vo.GuestBookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestBookServiceImpl implements GuestBookService{
    @Autowired
    private GuestBookMapper guestBookMapper;

    @Override
    public List<GuestBookVO> guestBookList() {
        return guestBookMapper.guestBookList();
    }

    @_____
    _____ _____ _____(_____ _____) {
        _____ _____._____(_____);
    }

    @Override
    public int guestBookInsert(GuestBookVO gvo) {
        return guestBookMapper.guestBookInsert(gvo);
    }

    @Override
    public int guestBookDelete(String g_idx) {
        return guestBookMapper.guestBookDelete(g_idx);
    }

    @Override
    public int guestBookUpdate(GuestBookVO gvo) {
        return guestBookMapper.guestBookUpdate(gvo);
    }
}
