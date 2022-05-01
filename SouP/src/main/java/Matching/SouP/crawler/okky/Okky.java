package Matching.SouP.crawler.okky;

import lombok.Getter;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Getter
@Entity
public class Okky {
    protected Okky() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Project_SEQ")
    private Long id;

    private int num;  //게시글 번호로 중복 크롤링 방지 예정
    private String postName;
    private String content;
    private String userName;
    private String date;
    private String link;
    private String stack;
    private int views;
    private String talk; //연락 링크
    private boolean End = false;  //마감 여부

    public Okky(String num, String postName, String content, String userName, String date, String link, String stack, int views,  String talk) {
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
    @Transactional
    public void updateViews(int views){
        this.views = views;
    }
}
