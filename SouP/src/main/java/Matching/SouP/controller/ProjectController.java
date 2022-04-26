package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.favForm;
import Matching.SouP.dto.project.PostForm;
import Matching.SouP.repository.PostsRepository;
import Matching.SouP.repository.UserRepository;
import Matching.SouP.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;
    private final EntityManager em;


    @PostMapping("/project/build")
    public JSONObject saveProject(@LoginUser SessionUser user, @RequestBody PostForm pForm){
        Optional<User> User = userRepository.findByEmail(user.getEmail());
        JSONObject obj = new JSONObject();
        if(User.isPresent()){
            Long temp = projectService.tempSave(pForm,User.get());//왜 temp 냐면  사람과 연결을 안해서.
            obj.put("success", true);
        }
        else{
            log.warn("존재하지 않는 회원이 게시글을 올리고 있습니다.");
            obj.put("success", false);
        }
        obj.put("user",User.get());
        return obj;
    }


    @PostMapping("/project/fav")  //스크랩은 project_Connect 에 있는 거 긁어오면됌
    public JSONObject fav(@LoginUser SessionUser user, @RequestBody favForm form){
        User User = userRepository.findByEmail(user.getEmail()).orElseThrow();
        return projectService.fav(User, form);
    }

    @Transactional
    @RequestMapping("/projects/query")
    public List<Post> arrange(@RequestParam(required = false,defaultValue = "") List<String> stacks){
        String str="select p from Post p where ";
        System.out.println(stacks.size());
        for (String stack : stacks) {
            System.out.print(stack+"  ");
        }
        if(stacks.size()==1){
            str += "p.stack like '%"+stacks.get(0)+"%'";
        }
        else if(stacks.size()==2){
            str += "p.stack like '%"+stacks.get(0)+"%' and p.stack like '%"+stacks.get(1)+"%'";  //p.stack like '%spring%' and p.stack like '%react%'
        }
        else if(stacks.size()==3){
            str += "p.stack like '%"+stacks.get(0)+"%' and p.stack like '%"+stacks.get(1)+"%' and p.stack like '%"+stacks.get(2)+"%'"; //p.stack like '%spring%' and p.stack like '%react%' and p.stack like '%js%'
        }
        else{
            System.out.println("오류");
            str="select p from Post p";
        }
        str+=" order by p.date desc";
        System.out.println(str);
        return em.createQuery(str, Post.class).getResultList();
    }


//    @PostMapping("/project/edit")
//    public String updateProject(@LoginUser SessionUser user, @RequestBody PostForm pForm, @PathVariable Long id){
//        projectService.editProject(user, editForm, id);
//        System.out.println("수정완료");
//        return "redirect:/project";
//    }

//    @PostMapping("/project/delete/{id}")
//    public String delete(@PathVariable Long id){
//        projectService.deleteProject(id);
//        log.info("delete Controller");
//        return "redirect:/project"; //리다이렉트
//    }
//
//
//    @GetMapping("/projectList")
//    public String ProjectList(Model model){
//        List<ProjectInfo> tempList = projectService.tempFindList();
//        model.addAttribute("projectList",tempList);
//        return "projectList";
//    }



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
//        }  //필요없음
//    }
}