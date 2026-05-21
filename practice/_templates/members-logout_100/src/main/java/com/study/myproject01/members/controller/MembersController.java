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

    // 회원가입: body로 받은 MembersVO 의 비번 BCrypt 암호화 후 DB insert
    @PostMapping("/register")
    public DataVO getRegister(@RequestBody MembersVO mvo){
        DataVO dataVO = new DataVO();
        // [로그] 회원가입 시도 기록 — 비번은 아직 평문이라 절대 로그에 박지 X
        log.info("회원가입 시도: m_id={}", mvo.getM_id());
        try{
            // 1) 비밀번호 BCrypt 암호화
            // 평문 비번 → BCrypt 암호화. encode() 후엔 원본 복원 불가 (단방향 해시)
            mvo.setM_pw(passwordEncoder.encode(mvo.getM_pw()));
            // 2) DB 저장
            membersService.register(mvo);
            // 3) 성공 응답 세팅
            dataVO.setSuccess(Boolean.TRUE);
            dataVO.setMessage("회원가입 성공");
            // [로그] 회원가입 성공 감사 로그 — 누가 언제 가입했는지 기록
            log.info("회원가입 성공: m_id={}", mvo.getM_id());
        }catch (Exception e){
            // [로그] catch 블록 = 예측 못 한 예외 (ex. DB 중복 키, 연결 오류). error 레벨 + e 같이 박아야 스택트레이스 보임
            log.error("회원가입 처리 중 예외: m_id={}", mvo.getM_id(), e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("서버 오류 : " +e.getMessage());
        }
        return dataVO;
    }

    // 로그인: 아이디·비번 검증 → 토큰 발급 → refresh DB 저장 → 클라이언트에 토큰+회원정보 응답
    @PostMapping("/login")
    public DataVO getLogin(@RequestBody MembersVO mvo){
        DataVO dataVO = new DataVO();
        // [로그] 로그인 시도 자체를 기록 — 누가 어떤 아이디로 시도했는지 추적용 (비번은 절대 박지 X)
        log.info("로그인 시도: m_id={}", mvo.getM_id());
        try{
            // 1) 아이디 존재 여부 확인 (DB 조회 — null이면 없는 아이디로 early return)
            MembersVO membersVO = membersService.findById(mvo.getM_id());

            if(membersVO == null){
                // [로그] 없는 아이디로 시도 = brute-force 탐지 신호. warn 레벨로 알람 박는 자리.
                log.warn("로그인 실패 - 없는 아이디: m_id={}", mvo.getM_id());
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("없는 아이디 입니다");
                return dataVO;
            }
            // 2) 비밀번호 검증 (BCrypt matches — 단방향이라 복호화 불가, 해시끼리 비교)
            if(!passwordEncoder.matches(mvo.getM_pw(),membersVO.getM_pw())){
                // [로그] 비번 틀림도 보안 이벤트. 반복되면 brute-force 의심.
                log.warn("로그인 실패 - 비밀번호 불일치: m_id={}", mvo.getM_id());
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("비밀번호가 틀렸습니다.");
                return dataVO;
            }

            // 3) 토큰 생성 (access + refresh 둘 다)
            String accessToken = jwtUtil.generateAccessToken(membersVO.getM_id());
            String refreshToken = jwtUtil.generateRefreshToken(membersVO.getM_id());

            // 4) 기존 refreshToken 삭제 (중복 로그인 방지 — 항상 최신 토큰 1개만 유지)
            membersService.deleteRefreshToken(membersVO.getM_id());

            // 5) 새 refreshToken DB 저장
            RefreshTokenVO  refreshTokenVO = new RefreshTokenVO();
            refreshTokenVO.setRt_user_id(membersVO.getM_id());
            refreshTokenVO.setRt_token(refreshToken);
            membersService.saveRefreshToken(refreshTokenVO);

            // 6) 응답 맵 구성 (accessToken + refreshToken + 회원정보)
            Map<String,Object> map = new HashMap<>();
            map.put("accessToken",accessToken);
            map.put("refreshToken",refreshToken);
            map.put("membersVO",membersVO);

            // 7) 성공 응답 세팅
            dataVO.setSuccess(true);
            dataVO.setMessage("로그인 성공");
            dataVO.setData(map);

            // [로그] 로그인 성공 감사 로그 — 누가 언제 들어왔는지 기록
            log.info("로그인 성공: m_id={}", membersVO.getM_id());

        } catch (Exception e) {
            // [로그] catch 블록 = 예측 못 한 예외. error 레벨 + 예외 객체 e 같이 박아야 스택트레이스 보임
            log.error("로그인 처리 중 예외: m_id={}", mvo.getM_id(), e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("서버 오류 : " + e.getMessage());
        }

        return  dataVO;
    }

    // 토큰 재발급: refreshToken 검증 → 새 토큰 2개 발급 → DB 토큰 로테이션 (만료 시 catch 에서 재로그인 유도)
    @PostMapping("/refresh")
    public DataVO getRefreshToken(@RequestBody Map<String, String> body){
        DataVO dataVO = new DataVO();
        try{
            // 1) refreshToken 추출
            String refreshToken = body.get("refreshToken");

            // 2) 빈값 체크 (body에 토큰 없으면 잘못된 요청 — 정상 클라이언트라면 발생 안 함)
            if(refreshToken == null ||  refreshToken.isBlank()){
                // [로그] body에 토큰 자체가 없음 = 잘못된 요청. 정상 클라이언트라면 발생 안 함.
                log.warn("refresh 시도 - 토큰 없음 (잘못된 요청)");
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("refreshToken이 없네요");
                return dataVO;
            }

            // 3) DB에서 저장된 토큰 확인 (위조 토큰 차단 — JWT 서명만으론 탈취 여부 모름)
            RefreshTokenVO storeToken = membersService.findRefreshToken(refreshToken);
            if(storeToken == null){
                // [로그] DB에 없는 토큰으로 시도 = 누군가 위조 토큰 들고 옴. 보안 알람 박는 자리.
                log.warn("refresh 실패 - DB에 없는 토큰 (위조 가능성)");
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("유효하지 않는 refreshToken 입니다.");
                return dataVO;
            }

            // 4) JWT 서명+만료 검증 (DB 확인과 별개 — 두 관문 모두 통과해야 유효)
            String userId = jwtUtil.validateToken(refreshToken);
            if(userId == null){
                // [로그] DB엔 있는데 JWT 검증 실패 = 서명 위조 의심. DB 토큰 비교 + 서명 검증 둘 다 통과해야 안전.
                log.warn("refresh 실패 - JWT 검증 실패 (서명 위조 가능성)");
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("유효하지 않는 refreshToken 입니다.");
                return dataVO;
            }

            // 5) 새 토큰 생성 (access + refresh 둘 다)
            String newAccessToken = jwtUtil.generateAccessToken(userId);
            String newRefreshToken = jwtUtil.generateRefreshToken(userId);

            // 6) 토큰 로테이션 — 기존 삭제 후 새 refreshToken 저장 (재사용 방지)
            membersService.deleteRefreshToken(userId);
            RefreshTokenVO  newToken = new RefreshTokenVO();
            newToken.setRt_user_id(userId);
            newToken.setRt_token(newRefreshToken);
            membersService.saveRefreshToken(newToken);

            // 7) 새 토큰 응답
            Map<String,Object> map = new HashMap<>();
            map.put("accessToken",newAccessToken);
            map.put("refreshToken",newRefreshToken);

            dataVO.setSuccess(true);
            dataVO.setMessage("재발급 성공");
            dataVO.setData(map);

            // [로그] refresh 성공 = 토큰 로테이션 완료. 어떤 사용자가 갱신했는지 감사 로그.
            log.info("refresh 성공 - 토큰 로테이션: userId={}", userId);

        } catch (ExpiredJwtException e) {
            // refreshToken 만료 — DB 토큰 삭제 후 재로그인 유도 (access와 달리 refresh까지 만료되면 재인증 필수)
            String userId = e.getClaims().getSubject();
            membersService.deleteRefreshToken(userId);
            // [로그] 만료는 정상 흐름 (위조 아님). info 레벨로 박아서 통계용.
            log.info("refresh 만료 - 재로그인 필요: userId={}", userId);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("refreshToken 만료, 다시 로그인 해주세요");
        } catch (Exception e) {
            // [로그] 예측 못 한 예외 — error 레벨 + 예외 객체로 스택트레이스 보존
            log.error("refresh 처리 중 알 수 없는 예외", e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("refreshToken 오류");
        }
        return  dataVO;
    }

    // 로그아웃: DB 의 refresh 토큰 삭제 (access 토큰은 클라이언트에서 폐기)
    @_____ ("_____")
    _____ _____ _____(){
        _____ _____ = _____ _____();
        _____{
            // 1) SecurityContext에서 userId 추출
            _____ _____ = (_____)_____._____()._____()._____();
            // 2) DB에서 refreshToken 삭제 (서버 측 인증 무효화 — accessToken은 클라이언트가 폐기)
            _____._____(_____);
            // 3) 성공 응답 세팅
            _____._____(_____._____);
            _____._____("_____");
            // [로그] 로그아웃 성공 감사 로그 — 누가 언제 로그아웃했는지 기록
            _____._____("_____", _____);
        } _____ (_____ _____) {
            // [로그] catch 블록 = 예측 못 한 예외. error 레벨 + 예외 객체 e 같이 박아야 스택트레이스 보임
            _____._____("_____", _____);
            _____._____(_____._____);
            _____._____("_____");
        }
        _____ _____;
    }

    // 마이페이지: JWT 필터 통과 후 SecurityContext 에서 userId 꺼내 회원 정보 조회
    @GetMapping("/mypage")
    public DataVO getMyPage(){
        DataVO dataVO = new DataVO();
        try{
            // 1) SecurityContext에서 userId 추출 (JWT 필터가 이미 검증 완료 — 여기선 꺼내기만)
            String userId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // 2) 회원 정보 DB 조회
            MembersVO mvo = membersService.findById(userId);
            // 3) 결과에 따라 성공/실패 응답 세팅
            if(mvo == null){
                // [로그] 토큰은 유효한데 DB에 사용자 없음 = 계정 삭제됐는데 토큰 살아있음 = 이상 신호 (보안 알람)
                log.warn("마이페이지 - 토큰엔 있는데 DB에 없는 사용자: userId={}", userId);
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("없는 아이디 입니다.");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("마이페이지 성공");
                dataVO.setData(mvo);
            }
        } catch (Exception e) {
           // [로그] 예측 못 한 예외 — userId 컨텍스트 + 예외 객체 e 같이 박아서 추적 가능하게
           log.error("마이페이지 조회 중 예외", e);
           dataVO.setSuccess(Boolean.FALSE);
           dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    // 회원정보 수정: SecurityContext에서 userId 꺼내 VO에 세팅 후 DB update
    @PostMapping("/update")
    public DataVO getUpdate(@RequestBody MembersVO mvo){
        DataVO dataVO = new DataVO();
        try{
            // 1) SecurityContext에서 userId 추출
            String userId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // [로그] 회원정보 수정 시도 — userId는 토큰에서 꺼냄 (클라이언트 입력 신뢰 X)
            log.info("회원정보 수정 시도: m_id={}", userId);
            // 2) VO에 userId 세팅 (클라이언트가 id 안 보내도 서버가 토큰에서 직접 확인)
            mvo.setM_id(userId);
            // 3) DB update
            membersService.update(mvo);
            // 4) 성공 응답 세팅
            dataVO.setSuccess(Boolean.TRUE);
            dataVO.setMessage("회원정보 수정 성공");
            // [로그] 수정 성공 감사 로그
            log.info("회원정보 수정 성공: m_id={}", userId);
        } catch (Exception e) {
            // [로그] catch 블록 = 예측 못 한 예외. error 레벨 + e 같이 박아야 스택트레이스 보임
            log.error("회원정보 수정 중 예외", e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("서버 오류 : " + e.getMessage());
        }
        return dataVO;
    }

    // 회원 탈퇴: refresh 토큰 삭제(인증 무효화) → soft delete (m_active=1, row 삭제 X)
    @GetMapping("/delete")
    public DataVO getDelete(){
        DataVO dataVO = new DataVO();
        try{
            // 1) SecurityContext에서 userId 추출
            String userId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // [로그] 회원 탈퇴 시도 — 인증 무효화 먼저 해야 탈퇴 도중 재접근 차단
            log.info("회원 탈퇴 시도: userId={}", userId);
            // 2) refreshToken 삭제 (인증 무효화 먼저 — 탈퇴 도중 재접근 차단)
            membersService.deleteRefreshToken(userId);
            // 3) 회원 soft delete (m_active=1, 실제 row 삭제 X)
            membersService.delete(userId);
            // 4) 성공 응답 세팅
            dataVO.setSuccess(Boolean.TRUE);
            dataVO.setMessage("회원 탈퇴 성공");
            // [로그] 탈퇴 감사 로그
            log.info("회원 탈퇴 성공: userId={}", userId);
        } catch (Exception e) {
            // [로그] catch 블록 = 예측 못 한 예외. error 레벨 + e 같이 박아야 스택트레이스 보임
            log.error("회원 탈퇴 처리 중 예외", e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage("서버 오류 : " + e.getMessage());
        }
        return dataVO;
    }
}
