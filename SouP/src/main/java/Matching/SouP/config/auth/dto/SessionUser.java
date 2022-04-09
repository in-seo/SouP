package Matching.SouP.config.auth.dto;

import Matching.SouP.domain.user.User;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    @NotEmpty(message = "이메일이 있어야 합니다.")
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}