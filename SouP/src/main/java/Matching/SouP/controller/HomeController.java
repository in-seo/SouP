package Matching.SouP.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        log.info("home controller");
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
    public String profile() {
        log.info("profile controller");
        return "profile";
    }

    @RequestMapping("/lounge")
    public String lounge() {
        log.info("lounge controller");
        return "lounge";
    }


}