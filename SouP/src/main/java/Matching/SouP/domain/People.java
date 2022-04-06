package Matching.SouP.domain;

import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.project.Project_Question;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@SequenceGenerator(name = "People_SEQ_GEN",sequenceName = "People_SEQ") //초기 값 1, 재할당 50마다
public class People extends BaseTimeEntity{

    @Id
    @Column(name = "people_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "People_SEQ_GEN")
    private Long id;

    private String username; //닉네임

    private int age; //연령

    private String portfolio; //포폴 링크

    private String email;
    @OneToMany(mappedBy = "people")
    private List<Project_Question> questionList = new ArrayList<>(); //프로젝트에 단 댓글




    /**
     * 회원<->프로젝트 다대다 관계 잇기위해 생성
     **/
    @OneToMany(mappedBy = "people")
    private List<ProjectConnect> projectConnectList = new ArrayList<>();  //프로젝트-회원 엮여있는 리스트


}
