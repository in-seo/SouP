package Matching.SouP.domain.posts;

import Matching.SouP.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@ToString  //토이프로젝트 제작 후 테스트 및 홍보목적으로 자유롭게 글 게시 가능.
public class Lounge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lounge_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "lounge", cascade = CascadeType.REMOVE)
    private List<LoungeConnect> loungeConnectList = new ArrayList<>();

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
}
