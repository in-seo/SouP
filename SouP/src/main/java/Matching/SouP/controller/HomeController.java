package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.UserForm;
import Matching.SouP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {
    private final UserRepository userRepository;
    @RequestMapping("/")
    public String home(Model model, @LoginUser SessionUser user) {
        if(user != null){
            model.addAttribute("userName", user.getName());  //전달 하면 로그인 되어 있을 경우 로그인 화면 없앨 수 있음
        }
        return "index";
    }
    @GetMapping("/profile")
    public String profile(@LoginUser SessionUser user, Model model, RedirectAttributes attributes) {
        log.info("profile controller");
        if(user==null){
            attributes.addFlashAttribute("msg","회원가입 후 이용하실 수 있습니다.");
            return "redirect:/";
        }
        Optional<User> User = userRepository.findByEmail(user.getEmail());
        if(User.isPresent()){
            model.addAttribute("user",User.get());
            model.addAttribute("UForm",new UserForm(User.get().getId()));  //id를 post해서 이어쓰기위해서 함
            return "profile";
        }
        else{ //이메일 값 없이 등록한 사람
            return "redirect:/";
        }

    }

}