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
    public MembersVO findById(String mvo) {
        return membersMapper.findById(mvo.getM_Id);
    }


    @Override
    public MembersVO findByMember(MembersVO mvo) {
        return null;
    }

    @Override
    public MembersVO deleteRefreshToken(String m_id) {
        return membersMapper.deleteRefreshToken(m_id);
    }

    @Override
    public RefreshTokenVO saveRefreshToken(RefreshTokenVO refreshTokenVO) {
        return membersMapper.saveRefreshToken(refreshTokenVO);
    }


    @Override
    public RefreshTokenVO findRefreshToken(String refreshToken) {
        return membersMapper.findRefreshToken(refreshToken);
    }
}
