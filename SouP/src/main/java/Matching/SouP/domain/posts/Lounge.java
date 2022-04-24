package Matching.SouP.domain.posts;

import Matching.SouP.domain.BaseTimeEntity;
import Matching.SouP.domain.user.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Getter
@Entity
@ToString  //토이프로젝트 제작 후 테스트 및 홍보목적으로 자유롭게 글 게시 가능.
public class Lounge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Lounge(String content) {
        this.content = content;
    }

    protected Lounge() {
    }

    public void setUser(User user) {
        this.user = user;
        user.getLoungeList().add(this);
    }
}
