package Matching.SouP.domain.project;

import Matching.SouP.domain.BaseTimeEntity;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.user.User;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter @Setter
@ToString
public class Project_Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Question_id")
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 120)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public void setUser(User user){
        this.user = user;
        user.getQuestionList().add(this);
    }

    public void setPost(Post post){
        this.post= post;
        post.getQuestionList().add(this);
    }
}
