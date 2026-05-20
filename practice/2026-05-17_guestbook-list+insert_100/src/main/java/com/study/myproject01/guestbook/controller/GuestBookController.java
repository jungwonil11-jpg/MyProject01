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
    public DataVO guestBookList(){
        DataVO dataVO = new DataVO();
        try{
          List<GuestBookVO> guestbooklist =  guestBookService.getGuestBookList();
          if(guestbooklist == null || guestbooklist.isEmpty()){
              dataVO.setSuccess(Boolean.TRUE);
              dataVO.setMessage("리스트 없음");
          }else{
              dataVO.setSuccess(Boolean.TRUE);
              dataVO.setMessage("리스트 있음");
              dataVO.setData(guestbooklist);
          }
        }catch (Exception e){
           dataVO.setSuccess(Boolean.FALSE);
           dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    @PostMapping("/insert")
    public DataVO guestBookInsert(@RequestBody GuestBookVO gvo){
        DataVO dataVO = new DataVO();
        try{
           int guestbookinsert = guestBookService.getGuestBookInsert(gvo);
           if(guestbookinsert == 0){
               dataVO.setSuccess(Boolean.FALSE);
               dataVO.setMessage("실패");
           }else{
               dataVO.setSuccess(Boolean.TRUE);
               dataVO.setMessage("성공");
           }
        }catch (Exception e){
            dataVO.setData(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    @PostMapping("/detail")
    public DataVO getGuestBookDetail(@RequestBody GuestBookVO gvo){
        DataVO dataVO = new DataVO();
        try{

        }catch (Exception e){
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }
}
