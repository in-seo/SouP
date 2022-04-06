package Matching.SouP.dto.project;

import Matching.SouP.domain.project.form.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class EditForm {
    private String projectName;    // 프로젝트명
    private String text; // 프로젝트설명
    private String stack; // 기술분야
    private String data; //  참고자료
    private LocalDateTime upDate; // 수정시간
}
