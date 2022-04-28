package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.controller.exception.ErrorResponse;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.LoungeForm;
import Matching.SouP.dto.favForm;
import Matching.SouP.repository.UserRepository;
import Matching.SouP.service.LoungeService;
import Matching.SouP.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
public class apiController {

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
        try{
            Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
            if(optionalUser.isPresent()){
                User User = optionalUser.get();
                obj.put("success",true);
                obj.put("username",User.getName());
                obj.put("profileImage",User.getPicture());
            }
        }catch (NullPointerException e){
            obj.put("success",false);
        }finally {
            return obj;
        }
    }

    @GetMapping("/lounge")
    public JSONArray showLounge(@LoginUser SessionUser user){   //라운지 보여주기
        User User = userRepository.findByEmail(user.getEmail()).orElseThrow();
        return loungeService.showLounge(User);
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
        User User = userRepository.findByEmail(user.getEmail()).orElseThrow();
        return loungeService.fav(User, form);
    }



    @ExceptionHandler(NullPointerException.class)
    protected ErrorResponse handleException1() {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, "존재하지 않는 회원");
    }
    @ExceptionHandler(NoSuchElementException.class)
    protected ErrorResponse handleException2() {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, "존재하지 않는 회원");
    }

}
