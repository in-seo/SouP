package Matching.SouP.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity @ToString
@SequenceGenerator(name = "Posts_SEQ_GEN",sequenceName = "Posts_SEQ")
public class Post {
    protected Post(){}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "Posts_SEQ")
    private Long id;  //post번호

    @Column(name="post_id")
    private Long postId;  //기존 글 번호(신경 안써도 됌)

    @Column(nullable = false)
    private String postName;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    private String userName;
    private String date; //글 올라온 시간
    private String link;
    private String stack;
    private String views;
    private String talk;
    private boolean end = false;

    @Enumerated(value = EnumType.STRING)
    private Source source;  //ex) okky, inflearn, ...


    @Builder
    public Post(Long postId, String postName, String content, String userName, String date, String link, String stack, String views, String talk, Source source) {
        this.postId = postId;
        this.postName = postName;
        this.content = content;
        this.userName = userName;
        this.date = date;
        this.link = link;
        this.stack = stack;
        this.views = views;
        this.talk = talk;
        this.source = source;
    }




}
