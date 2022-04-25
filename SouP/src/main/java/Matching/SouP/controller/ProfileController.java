package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.UserForm;
import Matching.SouP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProfileController {
    private final UserRepository userRepository;

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
    @PostMapping("/profile")  //Post요청 시
    public String profile(UserForm userForm) {
        User user = userRepository.findById(userForm.getId()).get();
        user.updateProfile(userForm);  //이메일이 있다면 정식멤버 승인.
        sessionReset(user); //회원등급 GUEST -> USER로 변환
        return "redirect:/";
    }
    public void sessionReset(User user){
        /**
         * 지리는 코드다 시바;;;
         */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        for (GrantedAuthority updatedAuthority : updatedAuthorities) {
            System.out.println(updatedAuthority.getAuthority());
        }
        updatedAuthorities.add(new SimpleGrantedAuthority(user.getRoleKey())); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @PostMapping("/checkEmail")
    @ResponseBody
    public boolean checkEmail(@RequestParam("email") String email){
        Optional<User> Email = userRepository.findByEmail(email);
        boolean valid;
        if(Email.isPresent()) valid = false;  // 사용 불가능
        else valid = true;  // 이메일 사용 가능
        return valid;
    }

    @PostMapping("/checknick")
    @ResponseBody
    public boolean checknick(@RequestParam("nick") String nick){
        Optional<User> Nick = userRepository.findByNick(nick);
        boolean valid;
        if(Nick.isPresent()) valid = false;  // 사용 불가능
        else valid = true;  // 이메일 사용 가능
        return valid;
    }
}
