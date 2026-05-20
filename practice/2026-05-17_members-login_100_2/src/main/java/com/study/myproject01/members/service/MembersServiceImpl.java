package com.study.myproject01.members.service;

import com.study.myproject01.members.mapper.MembersMapper;
import com.study.myproject01.members.vo.MembersVO;
import com.study.myproject01.members.vo.RefreshTokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembersServiceImpl implements MembersService {
    @Autowired
    private MembersMapper membersMapper;

    @Override
    _____ _____ _____(_____ _____) {
        _____ _____._____(_____);
    }

    @Override
    public MembersVO findByMember(MembersVO mvo) {
        return null;
    }

    @Override
    _____ _____(_____ _____) {
       _____._____(_____);
    }

    @Override
    _____ _____(_____ _____) {
        _____._____(_____);
    }

    @Override
    public RefreshTokenVO findRefreshToken(String refreshToken) {
        return membersMapper.findRefreshToken(refreshToken);
    }
}
