package Matching.SouP.dto.project;

import Matching.SouP.domain.project.form.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class ProjectForm {
    private Project_Method method;  // 프로젝트 or 스터디
    private String name;    // 프로젝트명
    private Project_Type project_Type;  //프로젝트분야
    private MeetType meet_Type; // 대면 방식
    private Project_Place place;   // 대면 지역
    private Project_Platform platform; //출시 플랫폼
    private String text; // 프로젝트설명
    private String stack; // 기술분야
    private String data; //  참고자료
    private LocalDateTime ipDate; // 생성시간

}
