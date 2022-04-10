package Matching.SouP.domain.project;

import Matching.SouP.domain.BaseTimeEntity;

import Matching.SouP.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@SequenceGenerator(name = "Project_SEQ_GEN",sequenceName = "Project_SEQ", allocationSize = 1) //초기 값 1, 재할당 50마다
public class ProjectConnect extends BaseTimeEntity {  //다대다 연결 위한 테이블.

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "Project_SEQ")
    @Column(name = "project_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="projectInfo_id")
    private ProjectInfo projectInfo;

    public static ProjectConnect createConnect(ProjectInfo info, Matching.SouP.domain.user.User user){  //생성메서드
        ProjectConnect connect = new ProjectConnect();
        connect.setProjectInfo(info);
        connect.setUser(user);
        return connect;
    }


    public void setProjectInfo(ProjectInfo projectInfo){
        this.projectInfo =projectInfo;
        projectInfo.getProjectConnectList().add(this);
    }

    public void setUser(User user){
        this.user=user;
        user.getProjectConnectList().add(this);
    }


}
