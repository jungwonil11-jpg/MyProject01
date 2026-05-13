package com.study.myproject01.members.controller;

import com.study.myproject01.common.jwt.JwtUtil;
import com.study.myproject01.common.vo.DataVO;
import com.study.myproject01.members.service.MembersService;
import com.study.myproject01.members.vo.MembersVO;
import com.study.myproject01.members.vo.RefreshTokenVO;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Data;
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

    @Autowired MembersService membersService;


    // 임시: BCrypt 해시 확인용 (테스트 후 삭제)
    @GetMapping("/hash")
    public String hash(@RequestParam String pw) { return passwordEncoder.encode(pw); }

    @GetMapping("/hello")
    public String getHello() { return "Hello World! "; }

    @PostMapping("/hi")
    public String getHi(){
        return "Hi World! ";
    }

    @GetMapping("/hello2")
    public String getHello2(@RequestParam String msg) { return  msg + "님 Hello World! "; }

    @PostMapping("/hi2")
    public String getHi2(@RequestBody Map<String, String> body) { return body.get("msg")+"님 Hi World! "; }


    @PostMapping("/login")
    public DataVO getLogin(@RequestBody MembersVO mvo){
        DataVO dataVO = new DataVO();
        try{
            //아이디 존재 여부 확인
            MembersVO membersVO = membersService.findById(mvo.getM_id());

            if(membersVO == null){
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("없는 아이디 입니다.");
                return dataVO;
            }
            //비밀번호 검증
            if(!passwordEncoder.matches(mvo.getM_pw(), membersVO.getM_pw())){
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("비밀번호가 틀렸습니다.");
                return dataVO;
            }
            //토큰 생성
            String accessToken = jwtUtil.generateAccessToken(membersVO.getM_id());
            String refreshToken = jwtUtil.generateRefreshToken(membersVO.getM_id());

            // 기존 refresh token 삭제 후 새 토큰 저장 (중복 로그인 방지/ 항상 최신 토큰만 유지)
            membersService.deleteRefreshToken(membersVO.getM_id());
            //새로 만들어진 refresh token 저장
            RefreshTokenVO refreshTokenVO = new RefreshTokenVO();
            refreshTokenVO.setRt_user_id(membersVO.getM_id());
            refreshTokenVO.setRt_token(refreshToken);
            membersService.saveRefreshToken(refreshTokenVO);







            //클라이언트에게 보낼 정보 저장
            Map<String,Object> map = new HashMap<>();
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            map.put("membersVO",membersVO);

            //클라이언트에게 정보 보내기
            dataVO.setSuccess(true);
            dataVO.setMessage("로그인 성공");
            dataVO.setData(map);


        }catch(Exception e){
            log.error("로그인 오류: {}", e.getMessage());
            dataVO.setSuccess(false);
            dataVO.setMessage("서버 오류가 발생했습니다.");
        }
        return dataVO;
    }

    //mypage : 필터가 이미 토큰 검증 완료 -> SecurityContextHolder 에서 userId 바로 꺼냄
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

        }catch(Exception e){
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());

        }
        return dataVO;
    }

    // accessToken이 만료되어 클라이언트에서 refreshToken을 보내면 확인 후
    // accessToken과 refreshToken을 새로 생성 (refreshToken은 DB 저장)
    // 요청 body: {"refreshToken: "로그인시 받은 refreshToken"}
    @PostMapping("/refresh")
    public DataVO getRefreshToken(@RequestBody Map<String, String> body){
        DataVO dataVO = new DataVO();
        try{
            // 1) refreshToken 추출
            String refreshToken = body.get("refreshToken");

            // 2) 빈값 체크 : refreshToken을 body에 담지 않은 경우
            if(refreshToken == null || refreshToken.isBlank()){
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("refreshToken 없음");
                return dataVO;
            }

            // 3) DB에서 저장된 토큰 확인
            RefreshTokenVO refreshTokenVO = membersService.findRefreshToken(refreshToken);
            if(refreshTokenVO == null){
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("유효하지 않는 refreshToken 입니다.");
                return dataVO;
            }

            //이전 AccessToken으로 들어오면 체크?
            // 4) JWT 검증 (서명 + 만료)
            String userId = jwtUtil.validateToken(refreshToken);
            if(userId == null) {
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("유효하지 않는 refreshToken 입니다.");
                return dataVO;
            }

            //5) 새 토큰 생성
            String newAccessToken = jwtUtil.generateAccessToken(userId);
            String newRefreshToken = jwtUtil.generateRefreshToken(userId);

            //6) 토큰 로테이션 : 기존 refreshToken 삭제 후 새로운 refreshToken 저장
            membersService.deleteRefreshToken(userId);
            RefreshTokenVO newToken = new RefreshTokenVO();
            newToken.setRt_user_id(userId);
            newToken.setRt_token(newRefreshToken);
            membersService.saveRefreshToken(newToken);

            //새 토큰을 클라이언트에게 보낸다.
            Map<String, Object> map = new HashMap<>();
            map.put("accessToken", newAccessToken);
            map.put("refreshToken", newRefreshToken);

            dataVO.setSuccess(true);
            dataVO.setMessage("재발급 성공");
            dataVO.setData(map);



        } catch (ExpiredJwtException e) {
            // refreshToken 만료 시 여기로 이동
            // DB에서 삭제 후 재로그인 유도(refreshToken 까지 만료 되면 재로그인 해야 됨)
//            String userId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         String userId = e.getClaims().getSubject();
            membersService.deleteRefreshToken(userId);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("refreshToken 만료, 다시 로그인 해주세요");

        } catch (Exception e) {
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("refreshToken 오류");
        }
        return dataVO;
    }
}
