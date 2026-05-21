package com.study.myproject01.members.service;

import com.study.myproject01.members.vo.MembersVO;
import com.study.myproject01.members.vo.RefreshTokenVO;

public interface MembersService {

    // 회원가입
    void register(MembersVO mvo);

    MembersVO findById(String id);

    // refreshToken 삭제
    void deleteRefreshToken(String id);

    // 새로 만들어진 refresh token 저장
    void saveRefreshToken(RefreshTokenVO refreshTokenVO);

    // refreshToken을 받아서 DB에서 있는지 찾기
    RefreshTokenVO findRefreshToken(String refreshToken);

    // 회원정보 수정
    void update(MembersVO mvo);

    // 회원 탈퇴 (soft delete: m_active=1)
    void delete(String userId);
}
