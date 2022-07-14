package Matching.SouP.dto.project;

import lombok.Getter;
import org.json.simple.JSONObject;

@Getter
public class PostForm {
    private String title;    // 프로젝트명
    private JSONObject content; // 프로젝트 내용
}
