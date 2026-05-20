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
    public MembersVO findById(String id) {
        return membersMapper.findById(id);
    }

    @Override
    public MembersVO findByMember(MembersVO mvo) {
        return null;
    }

    @Override
    public void deleteRefreshToken(String id) {
       membersMapper.deleteRefreshToken(id);
    }

    @Override
    public void saveRefreshToken(RefreshTokenVO refreshTokenVO) {
        membersMapper.saveRefreshToken(refreshTokenVO);
    }

    @Override
    public RefreshTokenVO findRefreshToken(String refreshToken) {
        return membersMapper.findRefreshToken(refreshToken);
    }
}
