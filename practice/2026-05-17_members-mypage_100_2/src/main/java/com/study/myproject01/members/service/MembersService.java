package com.study.myproject01.members.service;

import com.study.myproject01.members.vo.MembersVO;
import com.study.myproject01.members.vo.RefreshTokenVO;

public interface MembersService {

    MembersVO findById(String id);
    MembersVO findByMember(MembersVO mvo);

    // refreshToken 관련
    void deleteRefreshToken(String id);
    // 새로 만들어진 refresh token 저장
    void saveRefreshToken(RefreshTokenVO refreshTokenVO);
    // refreshToken을 받아서 DB에서 있는지 찾기
    RefreshTokenVO findRefreshToken(String refreshToken);
}
