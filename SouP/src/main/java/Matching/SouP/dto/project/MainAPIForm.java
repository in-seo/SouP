package Matching.SouP.dto.project;

import lombok.Getter;

@Getter
public class MainAPIForm{
    private String postName;
    private String content;
    private Long id;

    public MainAPIForm(String postName, String content, Long id) {
        this.postName = postName;
        this.content = content;
        this.id = id;
    }
}
