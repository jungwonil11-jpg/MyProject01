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

    @_____(_____)
    public _____ getGuestBookInsert(@_____ _____ gvo){
        _____ dataVO = _____ _____();
        try{
           _____ result = guestBookService._____(gvo);
           if(result == 0){
               dataVO._____(_____);
               dataVO._____(_____);
           }else{
               dataVO._____(_____);
               dataVO._____(_____);
           }
        }catch (_____ e){
            dataVO._____(_____);
            dataVO._____(e._____());
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
