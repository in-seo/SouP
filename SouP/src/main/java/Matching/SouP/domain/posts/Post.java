package Matching.SouP.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private String stack;

    private String link;

    private String talk;

    private String ipDate; //글 올라온 시간

    @Enumerated(value = EnumType.STRING)
    private Source source;  //ex) okky, inflearn, ...

    @Builder
    public Post(Long id,String postName, String content, String userName, String stack, String link, String ipDate, String talk, Source source) {
        this.postId= id;
        this.postName = postName;
        this.content = content;
        this.userName = userName;
        this.stack = stack;
        this.link = link;
        this.ipDate = ipDate;
        this.talk = talk;
        this.source = source;
    }
}
