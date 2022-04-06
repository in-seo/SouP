package Matching.SouP.crawler.CamPick;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@Getter @ToString
@Entity
//@SequenceGenerator(name = "Project_SEQ_GEN",sequenceName = "Project_SEQ") //프로젝트랑 공유!
public class Campick {

    public Campick() {
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
    private String views;
    private String people;
    private String talk; //연락 링크
    private String region;
    private boolean End = false;  //마감 여부

    public Campick(int num, String postName, String content, String userName, String date, String views, String link, String people, String region) {
        this.num = num;
        this.postName = postName;
        this.content = content;
        this.userName = userName;
        this.date = date;
        this.views = views;
        this.link = link;
        this.people = people;
        this.region = region;
    }
//    private String stack;  후에 추가예정


}
