package com.study.myproject01.guestbook.controller;

import com.study.myproject01.common.vo.DataVO;
import com.study.myproject01.guestbook.service.GuestBookService;
import com.study.myproject01.guestbook.vo.GuestBookVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/guestbook")
public class GuestBookController {
    @Autowired
    private GuestBookService guestBookService;

    // 게시글 목록: 활성 게시글(g_active=0) 만 최신순으로 조회
    @GetMapping("/list")
    public DataVO getGuestBooklist(){
        DataVO dataVO = new DataVO();
        try{
            // 1) 활성 게시글 목록 DB 조회 (XML 에서 g_idx desc 정렬)
            List<GuestBookVO> gustbookList =  guestBookService.guestBookList();
            // 2) 결과에 따라 메시지 분기 (빈 리스트도 정상 응답 — 에러 아님)
            if(gustbookList == null || gustbookList.isEmpty()){
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("데이터가 없습니다");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("데이터 불러오기 성공");
                dataVO.setData(gustbookList);
            }
        }catch (Exception e){
            // [로그] catch 블록 = 예측 못 한 예외 (ex. DB 연결 끊김). error 레벨 + e 같이 박아야 스택트레이스 보임
            log.error("게시글 목록 조회 중 예외", e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    // 게시글 등록: body로 받은 GuestBookVO 를 DB insert (g_idx 는 AUTO_INCREMENT)
    @PostMapping("/insert")
    public DataVO getGuestBookInsert(@RequestBody GuestBookVO gvo){
        DataVO dataVO = new DataVO();
        // [로그] 게시글 등록 시도 기록 — 누가 어떤 글 올렸는지 추적용
        log.info("게시글 등록 시도: writer={}", gvo.getG_writer());
        try{
            // 1) DB insert (영향 받은 row 수 반환)
            int result = guestBookService.guestBookInsert(gvo);
            // 2) 결과에 따라 성공/실패 응답 세팅
            if(result == 0){
                // [로그] insert 가 0 row = DB 단에서 막힘 (제약 위반 등). warn 으로 박아서 알람
                log.warn("게시글 등록 실패: writer={}", gvo.getG_writer());
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("등록 실패");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("등록 성공");
                // [로그] 등록 성공 감사 로그 — 누가 언제 글 올렸는지 기록
                log.info("게시글 등록 성공: writer={}", gvo.getG_writer());
            }
        }catch (Exception e){
            // [로그] catch 블록 = 예측 못 한 예외. error 레벨 + e 같이 박아야 스택트레이스 보임
            log.error("게시글 등록 중 예외: writer={}", gvo.getG_writer(), e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    // 게시글 상세: body의 g_idx 로 활성 게시글 1건 조회
    @PostMapping("/detail")
    public DataVO getGuestBookDetail(@RequestBody GuestBookVO gvo){
        DataVO dataVO = new DataVO();
        // [로그] 상세 조회 시도 — 어떤 글 봤는지 추적용
        log.info("게시글 상세 조회 시도: g_idx={}", gvo.getG_idx());
        try{
            // 1) g_idx 로 활성 게시글 1건 조회 (없으면 null 반환)
            GuestBookVO detail = guestBookService.guestBookDetail(gvo.getG_idx());
            // 2) 결과에 따라 성공/실패 응답 세팅
            if(detail == null){
                // [로그] 없는 글 또는 비활성 글 시도 = 잘못된 g_idx 거나 이미 삭제된 글. warn 으로 박음
                log.warn("게시글 상세 조회 - 없는 게시글: g_idx={}", gvo.getG_idx());
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("데이터가 없습니다");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("상세 조회 성공");
                dataVO.setData(detail);
            }
        }catch (Exception e){
            // [로그] catch 블록 = 예측 못 한 예외. error 레벨 + e 같이 박아야 스택트레이스 보임
            log.error("게시글 상세 조회 중 예외: g_idx={}", gvo.getG_idx(), e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    // 게시글 수정: g_idx + g_writer 가 동시 일치할 때만 subject/content 변경 (본인 글만 수정 가능)
    @_____ ("_____")
    _____ _____ _____(@_____ _____ _____){
        _____ _____ = _____ _____();
        // [로그] 수정 시도 기록 — 누가 어떤 글 수정하려 했는지 추적용
        _____._____("_____", _____._____(), _____._____());
        _____{
            // 1) DB update (g_idx + writer + g_active=0 동시 만족해야 갱신)
            _____ _____ = _____._____(_____);
            // 2) 결과에 따라 성공/실패 응답 세팅
            _____(_____ _____ _____){
                // [로그] 0 row 갱신 = 본인 글 아니거나 이미 삭제된 글. warn 으로 박음 (권한 우회 시도 가능성)
                _____._____("_____", _____._____(), _____._____());
                _____._____(_____._____);
                _____._____("_____");
            }_____{
                _____._____(_____._____);
                _____._____("_____");
                // [로그] 수정 성공 감사 로그
                _____._____("_____", _____._____());
            }
        }_____ (_____ _____){
            // [로그] catch 블록 = 예측 못 한 예외. error 레벨 + e 같이 박아야 스택트레이스 보임
            _____._____("_____", _____._____(), _____);
            _____._____(_____._____);
            _____._____(_____._____());
        }
        _____ _____;
    }

    // 게시글 삭제: g_idx 로 soft delete (g_active=1 로 변경, row 삭제 X)
    @DeleteMapping("/delete/{g_idx}")
    public DataVO getGuestBookDelete(@PathVariable String g_idx){
        DataVO dataVO = new DataVO();
        // [로그] 삭제 시도 기록 — 어떤 글 삭제하려 했는지 추적용
        log.info("게시글 삭제 시도: g_idx={}", g_idx);
        try{
            // 1) DB soft delete (g_active=1 로 변경 — 실제 row 삭제 X)
            int result = guestBookService.guestBookDelete(g_idx);
            // 2) 결과에 따라 성공/실패 응답 세팅
            if(result == 0){
                // [로그] 0 row 갱신 = 없는 g_idx 또는 이미 삭제된 글. warn 으로 박음
                log.warn("게시글 삭제 실패: g_idx={}", g_idx);
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("삭제 실패");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("삭제 성공");
                // [로그] 삭제 성공 감사 로그
                log.info("게시글 삭제 성공: g_idx={}", g_idx);
            }
        }catch (Exception e){
            // [로그] catch 블록 = 예측 못 한 예외. error 레벨 + e 같이 박아야 스택트레이스 보임
            log.error("게시글 삭제 중 예외: g_idx={}", g_idx, e);
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }
}
