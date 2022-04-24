package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.domain.posts.Lounge;
import Matching.SouP.domain.project.Project_Question;
import Matching.SouP.domain.user.Role;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.LoungeForm;
import Matching.SouP.dto.project.QuestionForm;
import Matching.SouP.repository.LoungeRepository;

import Matching.SouP.repository.QuestionRepository;
import Matching.SouP.repository.UserRepository;
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

import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final ProjectService projectService;
    private final UserRepository userRepository;
    private final LoungeRepository loungeRepository;
    private final QuestionRepository questionRepository;

    @PostMapping("/project/{id}/addQuestion")
    public void addQuestion(@PathVariable Long id, @LoginUser SessionUser user, @RequestBody QuestionForm form){ //댓글 추가
        User presentUser = userRepository.findByEmail(user.getEmail()).get();
        Project_Question question = new Project_Question();
        question.setContent(form.getContent());
        projectService.addQuestion(id, presentUser.getId(),question);
    }

    @PostMapping("/lounge/add")
    @Transactional
    public void addLounge(@LoginUser SessionUser user, @RequestBody LoungeForm form){ //댓글 추가
//        Optional<User> User = userRepository.findByEmail(user.getEmail());
        Lounge lounge = new Lounge(form.getContent());
//        if(User.isPresent())
//            lounge.setUser(User.get());
//        else{
            User anonymous = new User("익명", "dd", "dd", Role.USER);
            userRepository.save(anonymous);
            lounge.setUser(anonymous);
//        }
        loungeRepository.save(lounge);
    }
    @GetMapping("/lounge")
    public JSONArray showLounge(){ //댓글 추가
        List<Lounge> loungeList = loungeRepository.findAllDesc();
        JSONArray arr = new JSONArray();
        for (Lounge lounge : loungeList) {
            JSONObject obj=new JSONObject();
            obj.put("user",lounge.getUser().getName());
            obj.put("content",lounge.getContent());
            obj.put("date",lounge.getCreatedDate().toString());
            arr.add(obj);
        }
        return arr;
    }
}
