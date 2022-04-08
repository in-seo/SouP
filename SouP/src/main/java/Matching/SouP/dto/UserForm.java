package Matching.SouP.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UserForm {
    private Long id;
    private String email;
    private String nickName;
    private String stack;

    public UserForm(Long id) {
        this.id = id;
    }
}
