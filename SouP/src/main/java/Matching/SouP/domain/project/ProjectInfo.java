package Matching.SouP.domain.project;

import Matching.SouP.domain.BaseTimeEntity;
import Matching.SouP.domain.project.form.*;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@SequenceGenerator(name = "ProjectInfo_SEQ_GEN",sequenceName = "ProjectInfo_SEQ") //초기 값 1, 재할당 50마다
@ToString
public class ProjectInfo extends BaseTimeEntity {
    public ProjectInfo(String projectName, String text, String stack, String data) {
        this.projectName = projectName;
        this.text = text;
        this.stack = stack;
        this.data = data;
    }//임시 생성자
    protected ProjectInfo() {
    }

    public ProjectInfo(ProjectInfo entity) {

        this.projectName = entity.getProjectName();
        this.method = entity.getMethod();
        this.platform = entity.getPlatform();
        this.text = entity.getText();
        this.stack = entity.getStack();
        this.data = entity.getData();
        this.type = entity.getType();
        this.meetType = entity.getMeetType();
        this.place = entity.getPlace();
    }

    @Id
    @Column(name = "projectInfo_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProjectInfo_SEQ")
    private Long id;


//    @OneToOne
//    @JoinColumn(name = "image_id")
//    private Project_image image; //이미지

    private String projectName; //닉네임

    @Enumerated(value = EnumType.STRING)
    private Project_Method method;  //프로젝트 OR 스터디  선택

    @Enumerated(value = EnumType.STRING)
    private Project_Platform platform;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String text;

    @Column(name = "project_stack")
    private String stack; //기술, 언어

    @Column(name = "project_data")
    private String data;  //참고자료

    @Enumerated(value = EnumType.STRING)
    private Project_Type type;  //프로젝트 분야

    @Enumerated(value = EnumType.STRING)
    private MeetType meetType;

    @Enumerated(value = EnumType.STRING)
    private Project_Place place;


    @OneToMany(mappedBy = "people")
    private List<Project_Question> questionList = new ArrayList<>(); //프로젝트에 단 댓글


    @OneToMany(mappedBy = "projectInfo")
    private List<ProjectConnect> projectConnectList = new ArrayList<>();  //프로젝트-회원 엮여있는 리스트  ***안쓸예정

}
