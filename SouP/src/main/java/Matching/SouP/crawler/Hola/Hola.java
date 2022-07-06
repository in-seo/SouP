package Matching.SouP.crawler.Hola;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@SequenceGenerator(name="Hola_SEQ_GEN",sequenceName = "Hola_SEQ")
public class Hola {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Hola_SEQ")
    private Long id;
    private String num;
    private String postName;
    private String content;
    private String userName;
    private String date;
    private String link;
    private String stack;
    private int views;
    private String talk; //연락 링크
    private boolean End = false;  //마감 여부

    public Hola(String num, String postName, String content, String userName, String date, String link, String stack, int views,  String talk) {
        this.num = num;
        this.postName = postName;
        this.content = content;
        this.userName = userName;
        this.date = date;
        this.link = link;
        this.stack = stack;
        this.views = views;
        this.talk = talk;
    }

    protected Hola() {
    }
    public void makeStandardDate(String date) {
        this.date = date;
    }
}
