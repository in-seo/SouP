package Matching.SouP.domain.project;

import Matching.SouP.domain.BaseTimeEntity;
import Matching.SouP.domain.People;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@SequenceGenerator(name = "Project_SEQ_GEN",sequenceName = "Project_SEQ", allocationSize = 1) //초기 값 1, 재할당 50마다
public class ProjectConnect extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "Project_SEQ")
    @Column(name = "project_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="people_id")
    private People people;

    @ManyToOne
    @JoinColumn(name="projectInfo_id")
    private ProjectInfo projectInfo;

    public static ProjectConnect createConnect(ProjectInfo info){  //생성메서드
        ProjectConnect connect = new ProjectConnect();
        connect.setProjectInfo(info);
        return connect;
    }


    public void setProjectInfo(ProjectInfo projectInfo){
        this.projectInfo =projectInfo;
        projectInfo.getProjectConnectList().add(this);
    }

    public void setPeople(People people){
        this.people=people;
        people.getProjectConnectList().add(this);
    }


}
