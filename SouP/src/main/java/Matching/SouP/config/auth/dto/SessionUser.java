package Matching.SouP.config.auth.dto;

import Matching.SouP.domain.user.User;
import lombok.Getter;
import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private Long id;
    private String email;
    private String role;

    public SessionUser(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRoleKey();
    }
}