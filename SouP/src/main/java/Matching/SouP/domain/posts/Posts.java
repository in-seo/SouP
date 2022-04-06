package Matching.SouP.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

//@NoArgsConstructor
//@Getter
//@Entity
//@SequenceGenerator(name = "Posts_SEQ_GEN",sequenceName = "Posts_SEQ")
//public class Posts {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "Posts_SEQ")
//    private Long id;
//
//    @Column(length = 30, nullable = false)
//    private String postName;
//    @Column(columnDefinition = "TEXT", nullable = false)
//    private String content;
//
//    private String userName;
//
//    @Builder
//    public Posts(String postName, String content, String userName) {
//        this.postName = postName;
//        this.content = content;
//        this.userName = userName;
//    }
//}
