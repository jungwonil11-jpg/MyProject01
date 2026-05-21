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
    public void register(MembersVO mvo) {
        membersMapper.register(mvo);
    }

    @Override
    public MembersVO findById(String id) {
        return membersMapper.findById(id);
    }

    @_____
    _____ _____ _____(_____ _____) {
       _____._____(_____);
    }

    @Override
    public void saveRefreshToken(RefreshTokenVO refreshTokenVO) {
        membersMapper.saveRefreshToken(refreshTokenVO);
    }

    @Override
    public RefreshTokenVO findRefreshToken(String refreshToken) {
        return membersMapper.findRefreshToken(refreshToken);
    }

    @Override
    public void update(MembersVO mvo) {
        membersMapper.update(mvo);
    }

    @_____
    _____ _____ _____(_____ _____) {
        _____._____(_____);
    }
}
