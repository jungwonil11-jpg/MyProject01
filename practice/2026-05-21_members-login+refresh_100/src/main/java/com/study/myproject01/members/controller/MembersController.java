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
        // [LEARN] 회원가입 시도 기록 — 비번은 아직 평문이라 절대 로그에 박지 X
        log.info("[LEARN] 회원가입 시도: m_id={}", mvo.getM_id());
        try{
            // [LEARN] 평문 비번 → BCrypt 암호화. encode() 후엔 원본 복원 불가 (단방향 해시)
            mvo.setM_pw(passwordEncoder.encode(mvo.getM_pw()));
            membersService.register(mvo);
            dataVO.setSuccess(Boolean.TRUE);
            dataVO.setMessage("회원가입 성공");
            // [LEARN] 회원가입 성공 감사 로그 — 누가 언제 가입했는지 기록
            log.info("[LEARN] 회원가입 성공: m_id={}", mvo.getM_id());
        }catch (Exception e){
            // [LEARN] catch 블록 = 예측 못 한 예외 (ex. DB 중복 키, 연결 오류). error 레벨 + e 같이 박아야 스택트레이스 보임
            log.error("[LEARN] 회원가입 처리 중 예외: m_id={}", mvo.getM_id(), e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("서버 오류 : " +e.getMessage());
        }
        return dataVO;
    }

    @PostMapping ("/login")
    public DataVO getLogin(@RequestBody MembersVO mvo){
        DataVO dataVO = new DataVO();
        // [LEARN] 로그인 시도 자체를 기록 — 누가 어떤 아이디로 시도했는지 추적용 (비번은 절대 박지 X)
        log.info("로그인 시도 : {}", mvo.getM_id());
        try {
            // 아이디 존재 여부 확인
            MembersVO membersVO = membersService.findById(mvo.getM_id());

            if(membersVO == null){
                // [LEARN] 없는 아이디로 시도 = brute-force 탐지 신호. warn 레벨로 알람 박는 자리.
                log.warn("없는 아이디로 시도한 아이디 : {}", mvo.getM_id());
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("아이디 혹은 비밀번호 일치하지 않음");
                return  dataVO;
            }
            // 비밀번호 검증
            if(!passwordEncoder.matches(mvo.getM_pw(),membersVO.getM_pw())){
                // [LEARN] 비번 틀림도 보안 이벤트. 반복되면 brute-force 의심.
                log.warn("비밀번호 틀린 아이디 : {}", mvo.getM_id());
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("아이디 혹은 비밀번호 일치하지 않음");
                return  dataVO;
            }

            // 토큰 생성
            String accessToken = jwtUtil.generateAccessToken(membersVO.getM_id());
            String refreshToken = jwtUtil.generateRefreshToken(membersVO.getM_id());

            // 기존 refresh token 삭제 후 새 토큰 저장 (중복 로그인 방지 / 항상 최신 토큰만 유지)
            membersService.deleteRefreshToken(membersVO.getM_id());

            // 새로 만들어진 refresh token 저장
            RefreshTokenVO  refreshTokenVO = new RefreshTokenVO();
            refreshTokenVO.setRt_user_id(membersVO.getM_id());
            refreshTokenVO.setRt_token(refreshToken);
            membersService.saveRefreshToken(refreshTokenVO);

            // 클라이언트에게 보낼 정보 저장
            Map<String,Object> map = new HashMap<>();
            map.put("accessToken",accessToken);
            map.put("refreshToken",refreshToken);
            map.put("membersVO",membersVO);

            // 클라이언트에게 정보 보내기
            dataVO.setSuccess(Boolean.TRUE);
            dataVO.setMessage("로그인 성공");
            dataVO.setData(map);

            // [LEARN] 로그인 성공 감사 로그 — 누가 언제 들어왔는지 기록
            log.info("로그인 성공 아이디 : {}", membersVO.getM_id());

        } catch (Exception e) {
            // [LEARN] catch 블록 = 예측 못 한 예외. error 레벨 + 예외 객체 e 같이 박아야 스택트레이스 보임
            log.error("예외 발생  : {}", e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("예외 발생 : {}" + e.getMessage());
        }

        return   dataVO;
    }

    // mypage : 필터가 이미 토큰 검증 완료 -> SecurityContextHolder에서 userId 바로 꺼냄
    @GetMapping("/myPage")
    public DataVO getMyPage(){
        DataVO dataVO = new DataVO();
        try{
            String userId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            MembersVO mvo = membersService.findById(userId);
            if(mvo == null){
                // [LEARN] 토큰은 유효한데 DB에 사용자 없음 = 계정 삭제됐는데 토큰 살아있음 = 이상 신호 (보안 알람)
                log.warn("[LEARN] 마이페이지 - 토큰엔 있는데 DB에 없는 사용자: userId={}", userId);
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("없는 아이디 입니다.");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("마이페이지 성공");
                dataVO.setData(mvo);
            }
        } catch (Exception e) {
           // [LEARN] 예측 못 한 예외 — userId 컨텍스트 + 예외 객체 e 같이 박아서 추적 가능하게
           log.error("[LEARN] 마이페이지 조회 중 예외", e);
           dataVO.setSuccess(Boolean.FALSE);
           dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    // accessToken 이 만료 되어 클라이언트에서  refreshToken을 보내면 확인 후
    // accessToken과 refreshToken을 새로 생성 (refreshToken는 DB  저장)
    // 요청 body: {"refreshToken: "로그인 시 받은 refreshToken"}
    @PostMapping ("/refresh")
    public  DataVO getRefreshToken(@RequestBody Map<String, String> body){
        DataVO dataVO = new DataVO();
        try {
            // 1) refreshToken 추출
            String refreshToken = body.get("refreshToken");

            // 2) 빈값 체크 : refreshToken을 body에 담지 않은 경우
            if(refreshToken == null ||  refreshToken.isBlank()){
                // [LEARN] body에 토큰 자체가 없음 = 잘못된 요청. 정상 클라이언트라면 발생 안 함.
                log.warn("잘못된 요청");
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("잘못된 요청");
                return  dataVO;
            }

            // 3) DB에서 저장된 토큰 확인
            RefreshTokenVO refreshTokenVO = membersService.findRefreshToken(refreshToken);
            if(refreshTokenVO == null){
                // [LEARN] DB에 없는 토큰으로 시도 = 누군가 위조 토큰 들고 옴. 보안 알람 박는 자리.
                log.warn("잘못된 요청");
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("잘못된 요청");
                return  dataVO;

            }

            // 4) JWT 검증 (서명+만료)
            String userId = jwtUtil.validateToken(refreshToken);
            if(userId == null){
                // [LEARN] DB엔 있는데 JWT 검증 실패 = 서명 위조 의심. DB 토큰 비교 + 서명 검증 둘 다 통과해야 안전.
                log.warn("JWT 검증 실패");
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("JWT 검증 실패");
                return  dataVO;
            }

            // 5) 새 토큰 생성 (둘 다)
            String newAccessToken = jwtUtil.generateAccessToken(userId);
            String newRefreshToken = jwtUtil.generateRefreshToken(userId);

            // 6) 토큰 로테이션: 기존 refreshToken 삭제 후 새로운 refreshToken 저장
            membersService.deleteRefreshToken(userId);
            RefreshTokenVO  refreshTokenVO1 = new RefreshTokenVO();
            refreshTokenVO1.setRt_user_id(userId);
            refreshTokenVO1.setRt_token(newRefreshToken);
            membersService.saveRefreshToken(refreshTokenVO1);

            // 7) 새 토큰을 클라이언트에게 보낸다.
            Map<String,Object> map = new HashMap<>();
            map.put("accessToken",newAccessToken);
            map.put("refreshToken",newRefreshToken);

            dataVO.setSuccess(Boolean.TRUE);
            dataVO.setMessage("토큰 갱신");
            dataVO.setData(map);

            // [LEARN] refresh 성공 = 토큰 로테이션 완료. 어떤 사용자가 갱신했는지 감사 로그.
            log.info("refresh 성공 아이디 {}", userId);

        } catch (ExpiredJwtException e) {
            // refreshToken 만료 시 여기로 이동
            // DB에서 삭제 후 재로그인 유도 (refreshToken 까지 만료 되면 재 로그인 해야 됨)
            // String userId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userId = e.getClaims().getSubject();
            membersService.deleteRefreshToken(userId);
            // [LEARN] 만료는 정상 흐름 (위조 아님). info 레벨로 박아서 통계용.
            log.info("토큰 만료 아이디 {}", userId);
            dataVO.setSuccess(Boolean.TRUE);
            dataVO.setMessage("토큰 만료");
        } catch (Exception e) {
            // [LEARN] 예측 못 한 예외 — error 레벨 + 예외 객체로 스택트레이스 보존
            log.error("예외 에러 {}", e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("외 에러");
        }
        return   dataVO;
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
            // [LEARN] 로그아웃 성공 감사 로그 — 누가 언제 로그아웃했는지 기록
            log.info("[LEARN] 로그아웃 성공: userId={}", userId);
        } catch (Exception e) {
            // [LEARN] catch 블록 = 예측 못 한 예외. error 레벨 + 예외 객체 e 같이 박아야 스택트레이스 보임
            log.error("[LEARN] 로그아웃 처리 중 예외", e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("로그아웃 실패");
        }
        return dataVO;
    }
}
