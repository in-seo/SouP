package Matching.SouP.domain.project;

import Matching.SouP.domain.BaseTimeEntity;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Getter @Setter
@SequenceGenerator(name = "Project_SEQ_GEN",sequenceName = "Project_SEQ", allocationSize = 1) //초기 값 1, 재할당 50마다
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

    @Transactional
    public ProjectConnect createConnect(Post post, User user){  //생성메서드
        setPost(post);
        setUser(user);
        return this;
    }


    public void setPost(Post post){
        this.post=post;
        post.getProjectConnectList().add(this);
    }

    public void setUser(User user){ //나중에하자
        this.user=user;
        user.getProjectConnectList().add(this);
    }


}
