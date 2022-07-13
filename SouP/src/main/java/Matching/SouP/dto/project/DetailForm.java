package Matching.SouP.dto.project;

import Matching.SouP.domain.post.Source;
import lombok.Getter;

@Getter
public class DetailForm extends ShowForm {

    private String type;
    private Long userId;

    public DetailForm(Long id, String postName, String content, String userName, String date, String link, String stack, int views, String talk, Source source, int fav, Long userId) {
        super(id, postName, content, userName, date, link, stack, views, talk, source, fav);
        this.userId = userId;
    }

    public void setType(String type) {
        this.type = type;
    }
}
