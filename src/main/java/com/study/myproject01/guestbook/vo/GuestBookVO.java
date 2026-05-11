package com.study.myproject01.guestbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * 방명록(guestbook) 테이블과 1:1로 대응하는 Value Object.
 *
 * <p>DB 컬럼명과 필드명을 일치시켜 MyBatis의 자동 매핑({@code resultType="GuestBookVO"})이
 * 동작하도록 설계되었다. application.yaml의 {@code type-aliases-package} 설정 덕분에
 * mapper XML에서 전체 경로 대신 "GuestBookVO"라는 단순 클래스명으로 참조할 수 있다.
 *
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
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestBookVO {

    /** 게시글 고유 번호 (AUTO_INCREMENT). DB에서 자동 생성되므로 INSERT 시 값을 넣지 않는다. */
    private String g_idx;

    /** 작성자 이름. */
    private String g_writer;

    /** 게시글 제목. */
    private String g_subject;

    /** 작성자 이메일 주소. */
    private String g_email;

    /** 게시글 수정·삭제 시 사용하는 비밀번호. 평문 저장 중 — 운영 환경에서는 해싱 필요. */
    private String g_pwd;

    /** 게시글 본문 내용. */
    private String g_content;

    /** 게시글 등록 일시 (문자열 형태로 조회). INSERT 시 SQL의 now()로 자동 입력된다. */
    private String g_regdate;

    /**
     * 게시글 활성 상태 플래그 (소프트 딜리트).
     * <ul>
     *   <li>"0" — 정상 게시글 (조회 대상)</li>
     *   <li>"1" — 삭제된 게시글 (조회에서 제외)</li>
     * </ul>
     * 실제 DELETE 쿼리 대신 이 값을 1로 업데이트하여 논리 삭제를 구현한다.
     */
    private String g_active;

    /** DB에 저장된 첨부 파일의 이름 (서버 저장 파일명). */
    private String f_name;

    /**
     * 클라이언트가 업로드한 첨부 파일 원본.
     * {@link MultipartFile}은 DB 컬럼이 아니라 폼 전송 시 파일 데이터를 받기 위한 필드이므로
     * MyBatis INSERT 파라미터에는 포함하지 않는다.
     */
    private MultipartFile file_name;
}
