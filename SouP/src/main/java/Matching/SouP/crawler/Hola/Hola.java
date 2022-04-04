package Matching.SouP.crawler.Hola;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter @Setter
@ToString
@Entity
@SequenceGenerator(name="Hola_SEQ_GEN",sequenceName = "Hola_SEQ")
public class Hola {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Hola_SEQ")
    private Long id;
    private String postName;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private String userName;
    private String date;
    private String link;
    private String views;
    private String stack;
    private String talk;
    private boolean End = false;  //마감 여부

    public Hola(String postName, String content, String userName, String date, String views, String link, String talk, String stack) {
        this.postName = postName;
        this.content = content;
        this.userName = userName;
        this.date = date;
        this.views = views;
        this.link = link;
        this.talk = talk;
        this.stack = stack;
    }

    public Hola() {

    }
}
