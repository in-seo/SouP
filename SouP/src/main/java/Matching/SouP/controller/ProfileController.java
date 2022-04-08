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
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProfileController {
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public String profile(@LoginUser SessionUser user, Model model) {
        log.info("profile controller");
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
    @PostMapping("/profile")
    public String profile(UserForm userForm) {
        System.out.println("userForm = " + userForm.toString());
        User user = userRepository.findById(userForm.getId()).get();
        user.updateProfile(userForm);  //이메일이 있다면 정식멤버 승인.
        return "redirect:/";
    }
}
