package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.controller.exception.ErrorResponse;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.LoungeForm;
import Matching.SouP.dto.FavForm;
import Matching.SouP.repository.UserRepository;
import Matching.SouP.service.LoungeService;
import Matching.SouP.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
public class LoungeController {
    private final UserService userService;
    private final LoungeService loungeService;

    @GetMapping("/lounge")
    public JSONArray showLounge(@LoginUser SessionUser user){   //라운지 보여주기
        try{
            User currentUser = userService.getUserWithLoungeFav(user.getEmail());
            return loungeService.showLoungeForUser(currentUser);
        }
        catch (NullPointerException e){
            return loungeService.showLoungeForGuest();
        }
    }


    @PostMapping("/lounge/add")
    public JSONObject addLounge(@LoginUser SessionUser user, @RequestBody LoungeForm form){  //라운지에 글 게시시 post로 요청받고 하는 일
        User User = userService.getUser(user.getEmail());
        return loungeService.addLounge(User, form);
    }


    @PostMapping("/lounge/fav")
    public JSONObject fav(@LoginUser SessionUser user, @RequestBody FavForm form){
        User currentUser = userService.getUserWithLoungeFav(user.getEmail());
        return loungeService.fav(currentUser, form);
    }


    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleException1() {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, "존재하지 않는 회원");
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleException2() {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, "존재하지 않는 회원");
    }

}
