package Matching.SouP.domain.post;

import Matching.SouP.domain.BaseTimeEntity;
import Matching.SouP.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Entity
@SequenceGenerator(name = "Lounge_SEQ_GEN",sequenceName = "Lounge_SEQ") //초기 값 1, 재할당 50마다
public class Lounge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Lounge_SEQ")
    @Column(name = "lounge_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "lounge", cascade = CascadeType.REMOVE)
    private List<LoungeConnect> loungeConnectList = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;  //작성자


    private int fav =0;

    public Lounge(String content) {
        this.content = content;
    }

    protected Lounge() {
    }

    public void plusFav() {
        fav++;
    }
    public void minusFav() {
        if(fav>0) fav--;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
