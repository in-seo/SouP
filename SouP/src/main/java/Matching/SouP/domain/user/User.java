package Matching.SouP.domain.user;

import Matching.SouP.config.auth.dto.CoveringUser;
import Matching.SouP.domain.BaseTimeEntity;
import Matching.SouP.domain.post.Lounge;
import Matching.SouP.domain.post.LoungeConnect;
import Matching.SouP.domain.post.Post;
import Matching.SouP.domain.project.ProjectConnect;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.*;


@Getter
@Entity
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
    private String origin;

    @Column
    private String nickName;

    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * 회원<->프로젝트 다대다 관계 잇기위해 생성
     **/
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ProjectConnect> projectConnectList = new ArrayList<>();  //프로젝트-회원 엮여있는 리스트  스크랩!!!!

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<LoungeConnect> loungeConnectList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Lounge> loungeList = new ArrayList<>();

    @Builder
    public User(String name, String email, String picture, String origin, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.origin = origin;
        this.role = role;
        this.nickName = name;
    }

    public User(CoveringUser user) {
        this.id = user.getUser_id();
        this.email = user.getEmail();
        if(user.getRole().equals("GUEST"))
            this.role = Role.GUEST;
        else
            this.role = Role.ADMIN;
    }

    protected User() {}

    public User update(String name, String picture){
        this.name =name;
        this.picture = picture;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    public void changeName(String name) {
        this.nickName = name;
    }

}
