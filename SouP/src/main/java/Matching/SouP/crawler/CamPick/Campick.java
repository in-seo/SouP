package Matching.SouP.crawler.CamPick;

import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Getter
@Entity
public class Campick {

    public Campick() {}

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
    private int views;
    private String talk; //연락 링크
    private String people;
    private String region;
    private boolean End = false;  //마감 여부

    public Campick(int num, String postName, String content, String userName, String date, String link, String stack, int views,  String talk, String people, String region) {
        this.num = num;
        this.postName = postName;
        this.content = content;
        this.userName = userName;
        this.date = date;
        this.views = views;
        this.link = link;
        this.stack = stack;
        this.talk = talk;
        this.people = people;
        this.region = region;

    }
    public void makeStandardDate(String date) {
        this.date = date;
    }

    @Transactional
    public void updateViews(int views){
        this.views = views;
    }

}
