package Matching.SouP.controller;

import Matching.SouP.domain.project.ProjectInfo;
import Matching.SouP.domain.project.form.*;
import Matching.SouP.dto.project.ProjectForm;
import Matching.SouP.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/buildProject")
    public String addProject(Model model) {
        init();
        model.addAttribute("PForm", new ProjectForm());
        log.info("Project controller");
        return "buildProject";
    }

    @PostMapping("/buildProject")
    public String saveProject(ProjectForm pForm){
        ProjectInfo newProject = new ProjectInfo(pForm.getName(),pForm.getExplain(),pForm.getStack(),pForm.getData());
        newProject.setMeetType(pForm.getMeeting());newProject.setMethod(pForm.getMethod());newProject.setPlace(pForm.getPlace());
        newProject.setType(pForm.getProject_Type());newProject.setPlatform(pForm.getPlatform());   // 그냥 폼 데이터들을 받는거.
//        newProject.setImage(project_image);
//        System.out.println(project_image.toString());
        projectService.tempSave(newProject); //왜 temp 냐면  사람과 연결을 안해서.
        System.out.println(pForm.toString());System.out.println(newProject.toString());
        return "redirect:/";
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

    public void init(){
        for (int i=0; i<5; i++){
            ProjectInfo project = new ProjectInfo("프로젝트 "+i,"설명 : "+i,"보유 기술:xxx"+i*10,"agowaenawio"+i);
            project.setMeetType(MeetType.NONE);
            project.setType(Project_Type.ETC);
            project.setPlace(Project_Place.SEOUL);
            project.setMethod(Project_Method.PROJECT);
            projectService.tempSave(project);
        }  //필요없음
    }
}