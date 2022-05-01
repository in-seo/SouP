package Matching.SouP.domain.project;

import Matching.SouP.domain.BaseTimeEntity;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ProjectConnect extends BaseTimeEntity {  //다대다 연결 위한 테이블.

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "Project_SEQ")
    @Column(name = "project_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    public static ProjectConnect createConnect(Post post, User user){  //생성메서드
        ProjectConnect connect = new ProjectConnect();
        connect.setPost(post);
        connect.setUser(user);
        return connect;
    }


    public void setPost(Post post){
        this.post=post;
    }

    public void setUser(User user){
        this.user=user;
    }


}
