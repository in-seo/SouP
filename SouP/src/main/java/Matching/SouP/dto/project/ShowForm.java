package Matching.SouP.dto.project;

import Matching.SouP.domain.posts.Source;
import lombok.Getter;

@Getter
public class ShowForm {

    private Long id;  //post번호
    private String postName;
    private String content;  //Source == SOUP 면  content == parse한거
    private String userName;
    private String date; //글 올라온 시간
    private String link;
    private String[] stacks;
    private int views=0;
    private String talk;
    private Source source;
    private int fav=0;
    private boolean isfav=false;

    public ShowForm(Long id, String postName, String content, String userName, String date, String link, String stack, int views, String talk, Source source, int fav) {
        this.id = id;
        this.postName = postName;
        this.content = content;
        this.userName = userName;
        this.date = date;
        this.link = link;
        this.stacks = stack.split(",|\\s+");
        this.views = views;
        this.talk = talk;
        this.source = source;
        this.fav = fav;
    }

    public void setIsfav(boolean isfav) {
        this.isfav = isfav;
    }

    public void setContent(String parse) {
        this.content = parse;
    }

}

