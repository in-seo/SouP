package Matching.SouP.dto.project;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MainAPIForm{
    private String postName;
    @Setter
    private String content;
    private Long id;

    public MainAPIForm(String postName, String content, Long id) {
        this.postName = postName;
        this.content = content;
        this.id = id;
    }
}
