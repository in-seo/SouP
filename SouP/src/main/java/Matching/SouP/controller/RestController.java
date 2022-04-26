package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.domain.project.Project_Question;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.LoungeForm;
import Matching.SouP.dto.favForm;
import Matching.SouP.dto.project.QuestionForm;
import Matching.SouP.repository.UserRepository;
import Matching.SouP.service.LoungeService;
import Matching.SouP.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final ProjectService projectService;
    private final UserRepository userRepository;
    private final LoungeService loungeService;

//    @PostMapping("/project/{id}/addQuestion")
//    public void addQuestion(@PathVariable Long id, @LoginUser SessionUser user, @RequestBody QuestionForm form){ //댓글 추가
//        User presentUser = userRepository.findByEmail(user.getEmail()).get();
//        Project_Question question = new Project_Question();
//        question.setContent(form.getContent());
//        projectService.addQuestion(id, presentUser.getId(),question);
//    }

    @GetMapping("/auth")
    public JSONObject showAuth(@LoginUser SessionUser user){
        JSONObject obj = new JSONObject();
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if(optionalUser.isPresent()){
            User User = optionalUser.get();
            obj.put("success",true);
            obj.put("nickname",User.getNickName());
            obj.put("profileImage",User.getPicture());
        }
        else
            obj.put("success",false);
        return obj;
    }

    @GetMapping("/lounge")
    public JSONArray showLounge(){   //라운지 보여주기
        return loungeService.showLounge();
    }

    @Transactional
    @PostMapping("/lounge/add")
    public JSONObject addLounge(@LoginUser SessionUser user, @RequestBody LoungeForm form){  //라운지에 글 게시시 post로 요청받고 하는 일
        User User = userRepository.findByEmail(user.getEmail()).orElseThrow();
        return loungeService.addLounge(User, form);

    }

    @Transactional
    @PostMapping("/lounge/fav")
    public JSONObject fav(@LoginUser SessionUser user, @RequestBody favForm form){
        System.out.println("user.getEmail() = " + user.getEmail());
        User User = userRepository.findByEmail(user.getEmail()).orElseThrow();
        return loungeService.fav(User, form);
    }
}
