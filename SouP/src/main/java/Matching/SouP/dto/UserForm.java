package Matching.SouP.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
public class UserForm {   //nickname 수정 폼
    private boolean mode;  //mode : true  --> 사용자 설정,  mode: false -->랜덤
    private String userName;
}
