package Matching.SouP.domain.posts;

import Matching.SouP.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity @ToString
@SequenceGenerator(name = "Notice_SEQ_GEN",sequenceName = "Notice_SEQ")
public class Notice extends BaseTimeEntity {
    protected Notice() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "Notice_SEQ")
    private Long id;

    @Column(nullable = false)
    private String postName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String userName="관리자";

    private int views=0;

    public Notice(String postName, String content) {
        this.postName = postName;
        this.content = content;
    }
}
