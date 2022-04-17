package Matching.SouP.service.project;


import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.project.ProjectInfo;
import Matching.SouP.domain.project.Project_Question;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.project.EditForm;
import Matching.SouP.dto.project.ProjectForm;
import Matching.SouP.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final UserRepository userRepository;
    private final ProjectConnectRepository projectConnectRepository;
    private final ProjectInfoRepository projectInfoRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public ProjectConnect addConnect(ProjectConnect connect, User user){  //임시 저장한 프로젝트 검수 완료 후 회원 연결.
        connect.setUser(user);   //인원 연결
        return connect;
    }

    @Transactional
    public Long tempSave(ProjectForm pForm, User user){  //프로젝트정보 임시저장, 사람과 연결 전
        ProjectInfo newProject = new ProjectInfo(pForm.getName(),pForm.getText(),pForm.getStack(),pForm.getLink());
        newProject.setMethod(pForm.getMethod());newProject.setType(pForm.getProject_Type());// 그냥 폼 데이터들을 받는거.
        ProjectInfo save = projectInfoRepository.save(newProject);
        ProjectConnect connect = ProjectConnect.createConnect(newProject, user);//작성자와 프로젝트 연결
        projectConnectRepository.save(connect);
        return save.getId();
    }

    public List<ProjectInfo> tempFindList(){
        return projectInfoRepository.findAll();
    }

    @Transactional
    public Long addQuestion(Long projectId,Long userId, Project_Question question){  //댓글추가메서드./  생성은따로만들예정
        ProjectInfo projectInfo = projectInfoRepository.findById(projectId).get();
        User writer = userRepository.findById(userId).get();
        question.setProjectInfo(projectInfo);  //어떤 프로젝트에 대한 질문인지?
        question.setUser(writer);  // 어떤 사람이 쓴 댓글인지?
        questionRepository.save(question);
        return question.getId();
    }

    @Transactional
    public ProjectInfo editProject(EditForm editForm,Long id){
        Optional<ProjectInfo> byId = projectInfoRepository.findById(id);
        if(byId.isPresent()) {
            System.out.println("진입");
            ProjectInfo edit = byId.get();
            System.out.println(editForm.toString());
            edit.setProjectName(editForm.getProjectName());edit.setText(editForm.getText());
            edit.setStack(editForm.getStack()); edit.setLink(editForm.getData());
            System.out.println(edit.toString());
            return edit;
        } else{
            System.out.println("해당 게시글이 없습니다. id="+ id);
            return byId.get();
        }
    }

    @Transactional
    public void deleteProject(Long id) {
        Optional<ProjectInfo> del1 = projectInfoRepository.findById(id);
        Optional<ProjectConnect> del2 = projectConnectRepository.findById(id);
        if(del1.isPresent() && del2.isPresent()){
            ProjectInfo delete1 = del1.get();
            ProjectConnect delete2 = del2.get();
            projectInfoRepository.delete(delete1);
            projectConnectRepository.delete(delete2);
        }
        else
            System.out.println("존재하지 않는 게시글");
    }


    public List<ProjectConnect> findProjectList(){
        return projectConnectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ProjectInfo> findAllDesc(){
        return projectInfoRepository.findAllDesc();//.stream().map(ProjectInfo-> new ProjectInfo(ProjectInfo)).collect(Collectors.toList()); //람다;;
    }

    @Transactional(readOnly = true)
    public ProjectConnect findProjectByName(String ProjectName){
        return projectConnectRepository.findProjectByName(ProjectName);
    }

    public Optional<ProjectInfo> findById(Long id){
        return projectInfoRepository.findById(id);
    }


}
