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

/**
 * 방명록 REST API 컨트롤러.
 *
 * <p>{@code @RestController}는 {@code @Controller} + {@code @ResponseBody}의 조합으로,
 * 메서드 반환값이 자동으로 JSON으로 직렬화되어 HTTP 응답 본문에 기록된다.
 *
 * <p>{@code @RequestMapping("/guestbook")}으로 이 컨트롤러의 모든 URL은
 * {@code /guestbook}을 공통 접두사로 갖는다.
 *
 * <p>모든 핸들러 메서드는 {@link DataVO}를 반환하여 일관된 응답 구조를 유지한다.
 * try-catch 블록으로 예외를 잡아 success=false와 오류 메시지를 내려준다.
 */
@RestController
@RequestMapping("/guestbook")
public class GuestBookController {

    /**
     * 서비스 Bean 주입. 비즈니스 로직 호출을 담당한다.
     * 인터페이스 타입으로 선언하여 구현체 교체 시 이 파일을 수정하지 않아도 된다.
     */
    @Autowired
    private GuestBookService guestBookService;

    /**
     * 방명록 전체 목록 조회 API.
     *
     * <p>요청: {@code GET /guestbook/list}
     * <p>응답 예시 (데이터 있음):
     * <pre>
     * {
     *   "success": true,
     *   "message": "데이터 불러오기 성공",
     *   "data": [ { "g_idx": "1", "g_writer": "홍길동", ... }, ... ]
     * }
     * </pre>
     *
     * @return 목록이 있으면 data에 List, 없으면 data=null에 안내 메시지 반환
     */
    @GetMapping("/list")
    public DataVO getGuestBooklist(){
        DataVO dataVO = new DataVO();
        try{
            List<GuestBookVO> gustbookList = guestBookService.guestBookList();

            if(gustbookList == null || gustbookList.isEmpty()){
                // 조회는 성공했지만 등록된 데이터가 없는 경우
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("데이터가 없습니다");
            }else{
                dataVO.setSuccess(Boolean.TRUE);
                dataVO.setMessage("데이터 불러오기 성공");
                dataVO.setData(gustbookList);
            }
        }catch (Exception e){
            // DB 연결 오류, SQL 오류 등 예상치 못한 예외 처리
            dataVO.setSuccess(Boolean.FALSE);
            dataVO.setMessage(e.getMessage());
        }
        return dataVO;
    }

    /**
     * 방명록 특정 게시글 상세 조회 API.
     *
     * <p>요청: {@code GET /guestbook/detail/{g_idx}}
     * <p>URL 경로에서 게시글 번호를 추출하여 조회한다.
     * 예) {@code GET /guestbook/detail/5} → g_idx = "5"인 게시글 반환
     *
     * @param g_idx URL 경로 변수({@code @PathVariable})로 전달받는 게시글 번호
     * @return 해당 게시글 VO 또는 없음 안내 메시지
     */
    @GetMapping("/detail/{g_idx}")
    public DataVO getGuestBookDetail(@PathVariable String g_idx){
        DataVO dataVO = new DataVO();
        try{
            GuestBookVO guestBookVO = guestBookService.guestBookDetail(g_idx);

            if(guestBookVO == null){
                // 해당 번호의 게시글이 존재하지 않거나 이미 삭제된 경우
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

    /**
     * 방명록 게시글 등록 API.
     *
     * <p>요청: {@code POST /guestbook/insert}
     * <p>요청 본문(JSON)을 {@link GuestBookVO}로 역직렬화하여 DB에 삽입한다.
     * {@code @RequestBody}는 HTTP 요청의 Content-Type이 {@code application/json}일 때 동작한다.
     *
     * <p>요청 본문 예시:
     * <pre>
     * {
     *   "g_writer": "홍길동",
     *   "g_subject": "안녕하세요",
     *   "g_content": "방명록 내용입니다.",
     *   "g_email": "hong@example.com",
     *   "g_pwd": "1234"
     * }
     * </pre>
     *
     * @param gvo 요청 본문에서 파싱된 방명록 데이터 VO
     * @return 등록 성공/실패 여부를 담은 DataVO (data 필드는 null)
     */
    @PostMapping("/insert")
    public DataVO getGuestBookInsert(@RequestBody GuestBookVO gvo){
        DataVO dataVO = new DataVO();
        try{
            // 서비스를 통해 INSERT 실행, result는 DB에 영향받은 행 수
            int result = guestBookService.guestBookWInsert(gvo);

            if(result == 0){
                // INSERT가 실행되었으나 영향받은 행이 0인 경우 (드물지만 처리)
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
