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

    @_____
    _____ _____ _____(_____ _____) {
        _____._____(_____);
    }

    @_____
    _____ _____ _____(_____ _____) {
        _____ _____._____(_____);
    }

    @_____
    _____ _____ _____(_____ _____) {
       _____._____(_____);
    }

    @_____
    _____ _____ _____(_____ _____) {
        _____._____(_____);
    }

    @_____
    _____ _____ _____(_____ _____) {
        _____ _____._____(_____);
    }

    @Override
    public void update(MembersVO mvo) {
        membersMapper.update(mvo);
    }

    @Override
    public void delete(String userId) {
        membersMapper.delete(userId);
    }
}
