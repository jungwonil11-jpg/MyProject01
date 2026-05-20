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

    @PostMapping("/register")
    public DataVO getRegister(@RequestBody MembersVO mvo){
        DataVO dataVO = new DataVO();
        try{
            mvo.setM_pw(passwordEncoder.encode(mvo.getM_pw()));
            membersService.register(mvo);
            dataVO.setSuccess(Boolean.TRUE);
            dataVO.setMessage("회원가입 성공");
        }catch (Exception e){
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("서버 오류 : " +e.getMessage());
        }
        return dataVO;
    }

    @PostMapping ("/login")
    public DataVO getLogin(@RequestBody MembersVO mvo){
        DataVO dataVO = new DataVO();
        // [LEARN] 로그인 시도 자체를 기록 — 누가 어떤 아이디로 시도했는지 추적용 (비번은 절대 박지 X)
        log.info("로그인시도", mvo.getM_id());
        try {
            // 아이디 존재 여부 확인
            MembersVO membersVO = membersService.findById(mvo.getM_id());

            if(membersVO == null){
                // [LEARN] 없는 아이디로 시도 = brute-force 탐지 신호. warn 레벨로 알람 박는 자리.
                log.warn("는 아이디로 시도", mvo.getM_id());
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("아이디 혹은 비밀번호를 확인하세요");
                return  dataVO;
            }
            // 비밀번호 검증
            if(!passwordEncoder.matches(mvo.getM_pw(),membersVO.getM_pw())){
                // [LEARN] 비번 틀림도 보안 이벤트. 반복되면 brute-force 의심.
                log.warn("비번 틀림", _____._____());
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("아이디 혹은 비밀번호를 확인하세요");
                return  dataVO;
            }

            // 토큰 생성
            String accessToken = jwtUtil.generateAccessToken(membersVO.getM_id());
            String refreshToken = jwtUtil.generateRefreshToken(membersVO.getM_id());

            // 기존 refresh token 삭제 후  새 토큰 저장 (중복 로그인 방지/ 항상 최신 토그만 유지)
            membersService.deleteRefreshToken(membersVO.getM_id());

            // 새로 만들어진 refresh token 저장
            RefreshTokenVO refreshTokenVO = new RefreshTokenVO();
            refreshTokenVO.setRt_user_id(membersVO.getM_id());
            refreshTokenVO.setRt_token(refreshToken);
            membersService.saveRefreshToken(refreshTokenVO);

            // 클라이언트에게 보낼 정보 저장
            Map<String,Object> map = new HashMap<>();
            map.put("accessToken",accessToken);
            map.put("refreshToken",refreshToken);
            map.put("_____",_____);

            // 클라이언트에게 정보 보내기
            dataVO.setSuccess(Boolean.TRUE);
            dataVO.setMessage("토큰 갱신");
            dataVO.setData(map);

            // [LEARN] 로그인 성공 감사 로그 — 누가 언제 들어왔는지 기록
            log.info("_____", _____._____());

        } catch (Exception e) {
            // [LEARN] catch 블록 = 예측 못 한 예외. error 레벨 + 예외 객체 e 같이 박아야 스택트레이스 보임
            log.error("예외 에러", _____._____(), _____);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("예외 에러" + e.getMessage());
        }

        return dataVO;
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
