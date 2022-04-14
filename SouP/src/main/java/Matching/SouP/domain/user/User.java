package Matching.SouP.domain.user;

import Matching.SouP.domain.BaseTimeEntity;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.project.Project_Question;
import Matching.SouP.dto.UserForm;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity @ToString
@SequenceGenerator(name = "User_SEQ_GEN",sequenceName = "User_SEQ")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_SEQ")
    @Column(name = "user_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String picture;

    @Column
    private String nickName;

    @Column
    private String stack;

    @Column
    private String favor;
    
    @Column
    private String portfolio; //포폴 링크

    @Column
    private int exp; //경험치  글을 보거나 댓글 달거나, 로그인 시?

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Project_Question> questionList = new ArrayList<>(); //프로젝트에 단 댓글

    /**
     * 회원<->프로젝트 다대다 관계 잇기위해 생성
     **/
    @OneToMany(mappedBy = "user")
    private List<ProjectConnect> projectConnectList = new ArrayList<>();  //프로젝트-회원 엮여있는 리스트

    @Builder
    public User(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture){
        this.name =name;
        this.picture = picture;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    @Transactional
    public User updateProfile(UserForm userForm){
        if(!userForm.getEmail().isEmpty())
            this.role=Role.USER;  //정식 승인
        this.email = userForm.getEmail();
        this.nickName = userForm.getNickName();
        this.stack = userForm.getStack();
        this.favor = userForm.getFavor();
        this.portfolio=userForm.getPortfolio();
        return this;
    }

}
