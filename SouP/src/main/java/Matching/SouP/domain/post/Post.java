package Matching.SouP.domain.post;

import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter @Entity
@SequenceGenerator(name = "Posts_SEQ_GEN",sequenceName = "Posts_SEQ")
public class Post {
    protected Post(){}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "Posts_SEQ")
    @Column(name = "post_id")
    private Long id;  //post번호

    @Column(name="source_id")
    private Long sourceId;  //기존 글 번호(신경 안써도 됌)

    @Column(nullable = false)
    private String postName;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    private String userName;
    private String date; //글 올라온 시간
    private String link;
    private String stack;
    private int views=0;
    private String talk;
    private boolean end = false;
    private int fav =0;
    @Column(columnDefinition = "TEXT")
    private String prosemirror="";
    @Enumerated(value = EnumType.STRING)
    private Source source;  //ex) okky, inflearn, ...

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<ProjectConnect> projectConnectList = new ArrayList<>();  //프로젝트-회원 엮여있는 리스트  스크랩!!!!

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Post(Long sourceId, String postName, String content, String nickName, String date, String link, String stack, int views, String talk, Source source) {
        this.sourceId = sourceId;
        this.postName = postName;
        this.content = content;
        this.userName = nickName;
        this.date = date;
        this.link = link;
        this.stack = stack;
        this.views = views;
        this.talk = talk;
        this.source = source;
    }

    public void plusFav() {
        fav++;
    }
    public void minusFav() {
        if(fav>0) fav--;
    }

    public void setUser(User user) {
        user.getPostList().add(this);
        this.user = user;
    }

    public void setProsemirror(String prosemirror) {
        this.prosemirror = prosemirror;
    }

    public void addViews() {views+=2;}

    public void edit(String postName, String content){
        this.postName=postName;
        this.content=content;
    }
}
