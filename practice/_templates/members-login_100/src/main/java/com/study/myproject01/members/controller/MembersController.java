package com.study.myproject01.members.controller;

import com.study.myproject01.common.jwt.JwtUtil;
import com.study.myproject01.common.vo.DataVO;
import com.study.myproject01.members.service.MembersService;
import com.study.myproject01.members.vo.MembersVO;
import com.study.myproject01.members.vo.RefreshTokenVO;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/members")
public class MembersController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MembersService membersService;

    @_____ ("_____")
    _____ _____ _____(@_____ _____ _____){
        _____ _____ = _____ _____();
        // [LEARN] 로그인 시도 자체를 기록 — 누가 어떤 아이디로 시도했는지 추적용 (비번은 절대 박지 X)
        _____._____("_____", _____._____());
        _____{
            // 아이디 존재 여부 확인
            _____ _____ = _____._____(_____._____());

            _____(_____ _____ _____){
                // [LEARN] 없는 아이디로 시도 = brute-force 탐지 신호. warn 레벨로 알람 박는 자리.
                _____._____("_____", _____._____());
                _____._____(_____._____);
                _____._____("_____");
                _____ _____;
            }
            // 비밀번호 검증
            _____(!_____._____(_____._____(),_____._____())){
                // [LEARN] 비번 틀림도 보안 이벤트. 반복되면 brute-force 의심.
                _____._____("_____", _____._____());
                _____._____(_____._____);
                _____._____("_____");
                _____ _____;
            }

            // 토큰 생성
            _____ _____ = _____._____(_____._____());
            _____ _____ = _____._____(_____._____());

            // 기존 refresh token 삭제 후  새 토큰 저장 (중복 로그인 방지/ 항상 최신 토그만 유지)
            _____._____(_____._____());

            // 새로 만들어진 refresh token 저장
            _____ _____ = _____ _____();
            _____._____(_____._____());
            _____._____(_____);
            _____._____(_____);

            // 클라이언트에게 보낼 정보 저장
            _____<_____,_____> _____ = _____ _____<>();
            _____._____("_____",_____);
            _____._____("_____",_____);
            _____._____("_____",_____);

            // 클라이언트에게 정보 보내기
            _____._____(_____._____);
            _____._____("_____");
            _____._____(_____);

            // [LEARN] 로그인 성공 감사 로그 — 누가 언제 들어왔는지 기록
            _____._____("_____", _____._____());

        } _____ (_____ _____) {
            // [LEARN] catch 블록 = 예측 못 한 예외. error 레벨 + 예외 객체 e 같이 박아야 스택트레이스 보임
            _____._____("_____", _____._____(), _____);
            _____._____(_____._____);
            _____._____("_____" + _____._____());
        }

        _____ _____;
    }

    // mypage : 필터가 이미 토큰 검증 완료 -> SecurityContextHolder에서 userId 바로 꺼냄
    @GetMapping("/myPage")
    public DataVO getMyPage(){
        DataVO dataVO = new DataVO();
        try{
            String userId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            MembersVO mvo = membersService.findById(userId);
            if(mvo == null){
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("없는 아이디 입니다.");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("마이페이지 성공");
                dataVO.setData(mvo);
            }
        } catch (Exception e) {
           dataVO.setSuccess(Boolean.FALSE);
           dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    // accessToken 이 만료 되어 클라이언트에서  refreshToken을 보내면 확인 후
    // accessToken과 refreshToken을 새로 생성 (refreshToken는 DB  저장)
    // 요청 body: {"refreshToken: "로그인 시 받은 refreshToken"}
    @PostMapping("/refresh")
    public DataVO getRefreshToken(@RequestBody Map<String, String> body){
        DataVO dataVO = new DataVO();
        try{
            // 1) refreshToken 추출
            String refreshToken = body.get("refreshToken");

            // 2) 빈값 체크 : refreshTopken을 body엥 담지 않은 경우
            if(refreshToken == null ||  refreshToken.isBlank()){
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("refreshToken이 없네요");
                return dataVO;
            }

            // 3) DB에서 저장된 토큰 확인
            RefreshTokenVO storeToken = membersService.findRefreshToken(refreshToken);
            if(storeToken == null){
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("유효하지 않는 refreshToken 입니다.");
                return dataVO;
            }

            // 이전 AccessToken으로 들어오면 체크 ?
            // 4) JWT 검증 (서명+만료)
            String userId = jwtUtil.validateToken(refreshToken);
            if(userId == null){
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("유효하지 않는 refreshToken 입니다.");
                return dataVO;
            }

            // 5) 새 토큰 생성 (둘 다)
            String newAccessToken = jwtUtil.generateAccessToken(userId);
            String newRefreshToken = jwtUtil.generateRefreshToken(userId);

            // 6) 토큰 로테이션: 기존 refreshToken 삭제 후 새로운 refreshToken 저장
            membersService.deleteRefreshToken(userId);
            RefreshTokenVO  newToken = new RefreshTokenVO();
            newToken.setRt_user_id(userId);
            newToken.setRt_token(newRefreshToken);
            membersService.saveRefreshToken(newToken);

            // 7) 새 토큰을 클라이어트에게 보내다.
            Map<String,Object> map = new HashMap<>();
            map.put("accessToken",newAccessToken);
            map.put("refreshToken",newRefreshToken);

            dataVO.setSuccess(true);
            dataVO.setMessage("재발급 성공");
            dataVO.setData(map);


        } catch (ExpiredJwtException e) {
            // refreshToken 만료 시 여기로 이동
            // DB에서 삭제 후 재로그인 유도 (refreshToken 까지 만료 되면 재 로그인 해야 됨)
            // String userId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userId = e.getClaims().getSubject();
            membersService.deleteRefreshToken(userId);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("refreshToken 만료, 다시 로그인 해주세요");
        } catch (Exception e) {
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("refreshToken 오류");
        }
        return  dataVO;
    }

    // 로그아웃 : DB에서 refreshToken 삭제(accessToken은 클라이언트에서 삭제)
    @PostMapping("/logout")
    public DataVO getLogout(){
        DataVO dataVO = new DataVO();
        try{
            String userId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            membersService.deleteRefreshToken(userId);

            dataVO.setSuccess(Boolean.TRUE);
            dataVO.setMessage("로그아웃 성공");
            log.info("로그아웃 성공");
        } catch (Exception e) {
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("로그아웃 실패");
            log.info("로그아웃 실패");
        }
        return dataVO;
    }
}
