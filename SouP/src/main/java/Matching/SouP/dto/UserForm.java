package Matching.SouP.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UserForm {   //profile 수정 폼
    private Long id;
    private String email;
    private String nickName;
    private String stack;
    private String favor;
    private String portfolio;

    public UserForm(Long id) {
        this.id = id;
    }
}
