package Matching.SouP.dto.project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@Getter
public class PostForm {
    private String title;    // 프로젝트명
    private JSONObject content; // 프로젝트 내용
}
