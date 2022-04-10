package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.domain.project.ProjectInfo;
import Matching.SouP.domain.project.form.*;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.project.EditForm;
import Matching.SouP.dto.project.ProjectForm;
import Matching.SouP.repository.UserRepository;
import Matching.SouP.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    private final UserRepository userRepository;

    @GetMapping("/project")
    public String project(Model model, @LoginUser SessionUser user){
        List<ProjectInfo> all = projectService.findAllDesc();
        model.addAttribute("p",all);
        if(user != null){
            model.addAttribute("userName", user.getName());
        }

        return "project";
    }

    @GetMapping("/buildProject")
    public String addProject(Model model) {
        model.addAttribute("PForm", new ProjectForm());
        log.info("Project controller");
        return "buildProject";
    }

    @PostMapping("/buildProject")
    public String saveProject(ProjectForm pForm, @LoginUser SessionUser user){
        Optional<User> User = userRepository.findByEmail(user.getEmail());
        if(User.isPresent()){
            Long temp = projectService.tempSave(pForm,User.get());//왜 temp 냐면  사람과 연결을 안해서.
            System.out.println(projectService.findById(temp).get().toString());
        }
        else
            System.out.println("존재하지 않는 회원이 게시글을 올리고 있습니다.");
        System.out.println(User.get().toString());
        return "redirect:/";
    }


    @GetMapping("/project/edit/{id}")
    public String Edit(@PathVariable Long id, Model model){
        model.addAttribute("EditForm", new EditForm());
        ProjectInfo oldProject = projectService.findById(id).get();
        model.addAttribute("old",oldProject);
        log.info("Edit Controller");
        return "editProject";
    }

    @PostMapping("/project/edit/{id}")
    public String updateProject(EditForm editForm, @PathVariable Long id){
        projectService.editProject(editForm, id);
        System.out.println("수정완료");
        return "redirect:/project";
    }

    @PostMapping("/project/delete/{id}")
    public String delete(@PathVariable Long id){
        projectService.deleteProject(id);
        log.info("delete Controller");
        return "redirect:/project"; //리다이렉트
    }


    @GetMapping("/projectList")
    public String ProjectList(Model model){
        List<ProjectInfo> tempList = projectService.tempFindList();
        model.addAttribute("projectList",tempList);
        return "projectList";
    }


    @ModelAttribute("methods")
    public Project_Method[] project_Method(){
        return Project_Method.values();
    } //프로젝트인지 스터디인지 체크할 때 사용

    @ModelAttribute("projectTypes")
    public List<Project_Type> project_type(){
        List<Project_Type> list = new ArrayList<>();
        for (Project_Type value : Project_Type.values()) {
            list.add(value);
        }
        return list;
    } //프로젝트 유형 선택시 사용
    @ModelAttribute("meetTypes")
    public List<MeetType> meet(){
        List<MeetType> list = new ArrayList<>();
        for (MeetType value : MeetType.values()) {
            list.add(value);
        }
        return list;
    } //미팅 방법
    @ModelAttribute("places")
    public List<Project_Place> place(){
        List<Project_Place> list = new ArrayList<>();
        for (Project_Place value : Project_Place.values()) {
            list.add(value);
        }
        return list;
    } //지역

    @ModelAttribute("platforms")
    public List<Project_Platform> platform(){
        List<Project_Platform> list = new ArrayList<>();
        for (Project_Platform value : Project_Platform.values()) {
            list.add(value);
        }
        return list;
    } //출시 플랫폼

    @PostConstruct
    public void init(){
        for (int i=0; i<5; i++){
            ProjectForm pForm = new ProjectForm();
            pForm.setName("프로젝트 "+i);
            pForm.setText("설명 : "+i);
            pForm.setStack("보유 기술:xxx"+i*10);
            pForm.setData("agowaenawio"+i);
            ProjectInfo project = new ProjectInfo("프로젝트 "+i,"설명 : "+i,"보유 기술:xxx"+i*10,"agowaenawio"+i);
            pForm.setMeet_Type(MeetType.NONE);
            pForm.setProject_Type(Project_Type.ETC);
            pForm.setPlace(Project_Place.SEOUL);
            pForm.setPlatform(Project_Platform.APP);
            pForm.setMethod(Project_Method.PROJECT);
            User user = new User();
            userRepository.save(user);
            projectService.tempSave(pForm,user);
        }  //필요없음
    }
}