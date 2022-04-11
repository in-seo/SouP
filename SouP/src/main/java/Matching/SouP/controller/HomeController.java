package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.domain.project.ProjectInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(Model model, @LoginUser SessionUser user) {
        log.info("home controller");
        if(user != null){
            model.addAttribute("userName", user.getName());  //전달 하면 로그인 되어 있을 경우 로그인 화면 없앨 수 있음
        }
        return "index";
    }


    @RequestMapping("/login")
    public String login() {
        log.info("login controller");
        return "login";
    }

    @RequestMapping("/register")
    public String register() {
        log.info("regi controller");
        return "register";
    }

    @RequestMapping("/profile")
    public String profile(@LoginUser SessionUser user) {
        log.info("profile controller");


        return "profile";
    }

    @RequestMapping("/lounge")
    public String lounge() {
        log.info("lounge controller");
        return "lounge";
    }


}