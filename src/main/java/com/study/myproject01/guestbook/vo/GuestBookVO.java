package com.study.myproject01.guestbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>DB 테이블 구조 (참고):
 * <pre>
 * guestbook
 * ├── g_idx      INT AUTO_INCREMENT PRIMARY KEY  (게시글 고유 번호)
 * ├── g_writer   VARCHAR  (작성자 이름)
 * ├── g_subject  VARCHAR  (제목)
 * ├── g_email    VARCHAR  (이메일)
 * ├── g_pwd      VARCHAR  (수정·삭제 비밀번호)
 * ├── g_content  TEXT     (본문 내용)
 * ├── g_regdate  DATETIME (등록 일시, INSERT 시 now() 사용)
 * ├── g_active   TINYINT  (0: 정상, 1: 삭제됨 — 소프트 딜리트 방식)
 * └── f_name     VARCHAR  (업로드된 파일의 저장 이름)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestBookVO {
    private String g_idx;
    private String g_writer;
    private String g_subject;
    private String g_email;
    private String g_pwd;
    private String g_content;
    private String g_regdate;
    private String g_active;
    private String f_name;
    private MultipartFile file_name;
}
