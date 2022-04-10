package Matching.SouP.domain.project;

import Matching.SouP.domain.BaseTimeEntity;
import Matching.SouP.domain.user.User;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter @Setter
@ToString
@SequenceGenerator(name = "Question_SEQ_GEN",sequenceName = "Question_SEQ") //초기 값 1, 재할당 50마다
public class Project_Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Question_SEQ")
    @Column(name = "Question_id")
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 120)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "projectInfo_id")
    private ProjectInfo projectInfo;

    public void setUser(User user){
        this.user = user;
        user.getQuestionList().add(this);
    }

    public void setProjectInfo(ProjectInfo projectInfo){
        this.projectInfo = projectInfo;
        projectInfo.getQuestionList().add(this);
    }
}
