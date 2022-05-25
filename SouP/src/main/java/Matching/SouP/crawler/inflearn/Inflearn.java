package Matching.SouP.crawler.inflearn;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Inflearn {

    public Inflearn() {
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
    private int views=(int)Math.random()*100+1;
    private String talk; //연락 링크
    private boolean End = false;  //마감 여부

    public Inflearn(String num, String postName, String content, String userName, String date, String link, String stack, String talk) {
        this.num = Integer.parseInt(num);
        this.postName = postName;
        this.content = content;
        this.userName = userName;
        this.date = date;
        this.link = link;
        this.stack = stack;
        this.talk = talk;
    }
    public void makeStandardDate(String date) {
        this.date = date;
    }

}
