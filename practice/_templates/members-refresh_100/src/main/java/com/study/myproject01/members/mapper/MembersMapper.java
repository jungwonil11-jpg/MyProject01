package com.study.myproject01.members.mapper;

import com.study.myproject01.members.vo.MembersVO;
import com.study.myproject01.members.vo.RefreshTokenVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MembersMapper {
    // 회원가입
    void register(MembersVO mvo);

    // 아이디를 받아서 아이디가 있는지 확인
    MembersVO findById(String id);

    // refreshToken 삭제
    _____ _____(_____ _____);

    // 새로 만들어진 refresh token 저장
    _____ _____(_____ _____);

    // refreshToken을 받아서 DB에서 있는지 찾기
    _____ _____(_____ _____);

    // 회원정보 수정
    void update(MembersVO mvo);

    // 회원 탈퇴 (soft delete: m_active=1)
    void delete(String userId);
}
