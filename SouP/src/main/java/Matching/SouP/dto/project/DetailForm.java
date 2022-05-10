package Matching.SouP.dto.project;

import Matching.SouP.domain.posts.Source;

public class DetailForm extends ShowForm {

    private String type;

    public DetailForm(Long id, String postName, String content, String userName, String date, String link, String stack, int views, String talk, Source source, int fav) {
        super(id, postName, content, userName, date, link, stack, views, talk, source, fav);
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
