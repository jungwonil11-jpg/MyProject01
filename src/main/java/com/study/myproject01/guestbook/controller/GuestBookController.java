package com.study.myproject01.guestbook.controller;

import com.study.myproject01.common.vo.DataVO;
import com.study.myproject01.guestbook.service.GuestBookService;
import com.study.myproject01.guestbook.vo.GuestBookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/guestbook")
public class GuestBookController {
    @Autowired
    private GuestBookService guestBookService;

    @GetMapping("/list")
    public DataVO getGuestBooklist(){
        DataVO dataVO = new DataVO();
        try{
          List<GuestBookVO> gustbookList =  guestBookService.guestBookList();
          if(gustbookList == null || gustbookList.isEmpty()){
              dataVO.setSuccess(Boolean.TRUE);
              dataVO.setMessage("데이터가 없습니다");
          }else{
              dataVO.setSuccess(Boolean.TRUE);
              dataVO.setMessage("데이터 불러오기 성공");
              dataVO.setData(gustbookList);
          }
        }catch (Exception e){
           dataVO.setSuccess(Boolean.FALSE);
           dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    @PostMapping("/insert")
    public DataVO getGuestBookInsert(@RequestBody GuestBookVO gvo){
        DataVO dataVO = new DataVO();
        try{
           int result = guestBookService.guestBookInsert(gvo);
           if(result == 0){
               dataVO.setSuccess(Boolean.FALSE);
               dataVO.setMessage("등록 실패");
           }else{
               dataVO.setSuccess(Boolean.TRUE);
               dataVO.setMessage("등록 성공");
           }
        }catch (Exception e){
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    @PostMapping("/detail")
    public DataVO getGuestBookDetail(@RequestBody GuestBookVO gvo){
        DataVO dataVO = new DataVO();
        try{
            GuestBookVO detail = guestBookService.guestBookDetail(gvo.getG_idx());
            if(detail == null){
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("데이터가 없습니다");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("상세 조회 성공");
                dataVO.setData(detail);
            }
        }catch (Exception e){
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    @PutMapping("/update")
    public DataVO getGuestBookUpdate(@RequestBody GuestBookVO gvo){
        DataVO dataVO = new DataVO();
        try{
            int result = guestBookService.guestBookUpdate(gvo);
            if(result == 0){
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("수정 실패");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("수정 성공");
            }
        }catch (Exception e){
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    @DeleteMapping("/delete/{g_idx}")
    public DataVO getGuestBookDelete(@PathVariable String g_idx){
        DataVO dataVO = new DataVO();
        try{
            int result = guestBookService.guestBookDelete(g_idx);
            if(result == 0){
                dataVO.setSuccess(Boolean.FALSE);
                dataVO.setMessage("삭제 실패");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("삭제 성공");
            }
        }catch (Exception e){
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }
}
