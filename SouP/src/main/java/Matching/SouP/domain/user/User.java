package Matching.SouP.domain.user;

import Matching.SouP.domain.BaseTimeEntity;
import Matching.SouP.dto.UserForm;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@Entity
@ToString
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @NotEmpty(message = "이메일이 있어야 합니다.")
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
