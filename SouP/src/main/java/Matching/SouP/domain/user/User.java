package Matching.SouP.domain.user;

import Matching.SouP.dto.UserForm;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

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
        return this;
    }
}