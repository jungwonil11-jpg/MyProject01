package com.study.myproject01.guestbook.controller;

import com.study.myproject01.common.vo.DataVO;
import com.study.myproject01.guestbook.service.GuestBookService;
import com.study.myproject01.guestbook.vo.GuestBookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            List<GuestBookVO> gustbookList = guestBookService.guestBookList();
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

    @GetMapping("/detail/{g_idx}")
    public DataVO getGuestBookDetail(@PathVariable String g_idx){
        DataVO dataVO = new DataVO();
        try{
            GuestBookVO guestBookVO = guestBookService.guestBookDetail(g_idx);
            if(guestBookVO == null){
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("데이터가 없습니다");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("데이터 불러오기 성공");
                dataVO.setData(guestBookVO);
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
            int result = guestBookService.guestBookWInsert(gvo);
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


}
