package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.repository.UserRepository;
import Matching.SouP.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/mypost")
    public List<Post> showMyPost(@LoginUser SessionUser user){
        User User = userRepository.findByEmailFetchPC(user.getEmail()).orElseThrow();
        return userService.showPost(User);
    }

    @GetMapping("/myfav")
    public List<ProjectConnect> showMyFav(@LoginUser SessionUser user){
        User User = userRepository.findByEmailFetchPC(user.getEmail()).orElseThrow();
        return userService.showFav(User);
    }



}
