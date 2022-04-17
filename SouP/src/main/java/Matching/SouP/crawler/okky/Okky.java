package Matching.SouP.crawler.okky;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@Entity
//@SequenceGenerator(name = "Project_SEQ_GEN",sequenceName = "Project_SEQ") //프로젝트랑 공유!
public class Okky {
    protected Okky() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Project_SEQ")
    private Long id;

    private int num;  //게시글 번호로 중복 크롤링 방지 예정
    private String postName;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private String userName;
    private String date;
    private String link;
    private String stack;
    private String views;
    private String talk; //연락 링크
//    private String region;
    private boolean End = false;  //마감 여부

    public Okky(String num, String postName, String content, String userName, String date, String stack, String views, String link, String talk) {
        this.num = Integer.parseInt(num);
        this.postName = postName;
        this.content = content;
        this.userName = userName;
        this.date = date;
        this.stack = stack;
        this.views = views;
        this.link = link;
        this.talk = talk;
    }

    public void makeStandardDate(String date) {
        this.date = date;
    }
}
