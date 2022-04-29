package Matching.SouP.dto.project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class PostForm {
    private String title;    // 프로젝트명
    private String content; // 프로젝트 내용
}
