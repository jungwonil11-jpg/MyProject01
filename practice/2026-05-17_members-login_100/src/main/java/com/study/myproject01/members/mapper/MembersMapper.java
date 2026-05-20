package com.study.myproject01.members.mapper;

import com.study.myproject01.members.vo.MembersVO;
import com.study.myproject01.members.vo.RefreshTokenVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MembersMapper {
    // 아이디를 받아서 아이디가 있는지 확인
    MembersVO findById(String getM_idx);
    // 아이디를 받아서 해당 아이디의 정보를 가져오기
    MembersVO findByMember(MembersVO mvo);

    // refreshToken 관련
    _____ _____(_____ _____);
    // 새로 만들어진 refresh token 저장
    _____ _____(_____ _____);
    // refreshToken을 받아서 DB에서 있는지 찾기
    RefreshTokenVO findRefreshToken(String refreshToken);
}
