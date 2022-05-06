package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.controller.exception.ErrorResponse;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.favForm;
import Matching.SouP.dto.project.PostForm;
import Matching.SouP.dto.project.ShowForm;
import Matching.SouP.repository.UserRepository;
import Matching.SouP.service.PostService;
import Matching.SouP.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    private final PostService postService;
    private final UserRepository userRepository;



    @GetMapping("/projects")
    public PageImpl<ShowForm> projectList(@LoginUser SessionUser user, @RequestParam(required = false,defaultValue = "") List<String> stacks, Pageable pageable) {
        try{
            User User = userRepository.findByEmail(user.getEmail()).orElseThrow();
            return postService.projectListForUser(User,stacks, pageable);
        }
        catch (NullPointerException e){
            return postService.projectListForGuest(stacks,pageable);
        }
    }

    @PostMapping("/projects/build")
    public JSONObject saveProject(@LoginUser SessionUser user, @RequestBody PostForm pForm) throws ParseException {
        User User = userRepository.findByEmailFetchPL(user.getEmail()).orElseThrow();
        return projectService.tempSave(pForm,User);//왜 temp 냐면  사람과 연결을 안해서.
    }


    @PostMapping("/projects/fav")  //스크랩은 project_Connect 에 있는 거 긁어오면됌
    public JSONObject fav(@LoginUser SessionUser user, @RequestBody favForm form){
        User User = userRepository.findByEmailFetchPC(user.getEmail()).orElseThrow();
        return projectService.fav(User, form);
    }

    @GetMapping("/projects/{id}")
    public JSONObject showProject(@PathVariable Long id) throws ParseException {
        return postService.showProject(id);
    }



    @PostMapping("/projects/edit/{id}")
    public JSONObject updateProject(@LoginUser SessionUser user, @RequestBody PostForm pForm, @PathVariable Long id){
        User User = userRepository.findByEmail(user.getEmail()).orElseThrow();
        return projectService.editProject(User,pForm,id);
    }

    @PostMapping("/projects/delete/{id}")
    public JSONObject deleteProject(@LoginUser SessionUser user, @PathVariable Long id){
        User User = userRepository.findByEmail(user.getEmail()).orElseThrow();
        return projectService.deleteProject(User,id);
    }




//
//    @PostConstruct
//    public void init(){
//        for (int i=1; i<2; i++){
//            PostForm pForm = new PostForm();
//            pForm.setName("프로젝트 "+i);
//            pForm.setText("설명 : "+i);
//            pForm.setStack("보유 기술:xxx"+i*10);
//            pForm.setLink("agowaenawio"+i);
//            ProjectInfo project = new ProjectInfo("프로젝트 "+i,"설명 : "+i,"보유 기술:xxx"+i*10,"agowaenawio"+i);
//            pForm.setProject_Type(Project_Type.ETC);
//            pForm.setMethod(Project_Method.PROJECT);
//            User user = new User("샤","asjfla@gmail.com","asdfljaweifa.com", Role.USER);
//            userRepository.save(user);
//            projectService.tempSave(pForm,user);
//        }
//    }

    @ExceptionHandler(NullPointerException.class)
    protected ErrorResponse handleException1() {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, "존재하지 않는 회원");
    }
    @ExceptionHandler(NoSuchElementException.class)
    protected ErrorResponse handleException2() {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, "존재하지 않는 회원이거나 존재하지 않는 글에 요청을 합니다.");
    }
}