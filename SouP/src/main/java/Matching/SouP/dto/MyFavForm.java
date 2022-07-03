package Matching.SouP.dto;

import lombok.Getter;

@Getter
public class MyFavForm {
    private String postName;
    private String content;
    private String userName;
    private String link;

    public MyFavForm(String postName, String content, String userName, String link) {
        this.postName = postName;
        this.content = content;
        this.userName = userName;
        this.link = link;
    }
}
