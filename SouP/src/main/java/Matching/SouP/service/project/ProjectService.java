package Matching.SouP.service.project;


import Matching.SouP.domain.People;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.project.ProjectInfo;
import Matching.SouP.domain.project.Project_Question;
import Matching.SouP.dto.project.EditForm;
import Matching.SouP.repository.PeopleRepository;
import Matching.SouP.repository.ProjectConnectRepository;
import Matching.SouP.repository.ProjectInfoRepository;
import Matching.SouP.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final PeopleRepository peopleRepository;
    private final ProjectConnectRepository projectConnectRepository;
    private final ProjectInfoRepository projectInfoRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public ProjectConnect addConnect(ProjectConnect connect, People people){  //임시 저장한 프로젝트 검수 완료 후 회원 연결.
        connect.setPeople(people);   //인원 연결
        return connect;
    }

    @Transactional
    public Long tempSave(ProjectInfo projectInfo){  //프로젝트정보 임시저장, 사람과 연결 전
        projectInfo.setIpDate(LocalDateTime.now());
        ProjectInfo save = projectInfoRepository.save(projectInfo);
        ProjectConnect.createConnect(projectInfo);  //임시 연결
        return save.getId();
    }

    public List<ProjectInfo> tempFindList(){
        return projectInfoRepository.findAll();
    }

    @Transactional
    public Long addQuestion(Long projectId,Long peopleId, Project_Question question){  //댓글추가메서드./  생성은따로만들예정
        ProjectInfo projectInfo = projectInfoRepository.findById(projectId).get();
        People writer = peopleRepository.findById(peopleId);
        question.setProjectInfo(projectInfo);  //어떤 프로젝트에 대한 질문인지?
        question.setPeople(writer);  // 어떤 사람이 쓴 댓글인지?
        questionRepository.save(question);
        return question.getId();
    }

    public Long editProject(EditForm editForm,Long id){
        Optional<ProjectInfo> byId = projectInfoRepository.findById(id);
        if(byId.isPresent()) {
            ProjectInfo edit = byId.get();
            edit.setProjectName(editForm.getName());edit.setText(editForm.getText()); edit.setStack(editForm.getStack()); edit.setData(editForm.getData());
            edit.setMeetType(editForm.getMeeting());edit.setMethod(editForm.getMethod());edit.setPlace(editForm.getPlace());
            edit.setType(editForm.getProject_Type());edit.setPlatform(editForm.getPlatform());   // 그냥 폼 데이터들을 받는거.
        } else{
            System.out.println("해당 게시글이 없습니다. id="+ id);
        }
        return id;
    }


    public List<ProjectConnect> findProjectList(){
        return projectConnectRepository.findAll();
    }

    public ProjectConnect findProjectByName(String ProjectName){
        return projectConnectRepository.findProjectByName(ProjectName);
    }

    public Optional<ProjectInfo> findById(Long id){
        return projectInfoRepository.findById(id);
    }
}
