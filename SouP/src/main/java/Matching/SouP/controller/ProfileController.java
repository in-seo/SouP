package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.UserForm;
import Matching.SouP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final UserRepository userRepository;

    @GetMapping("/auth")
    public JSONObject showAuth(@LoginUser SessionUser user){
        JSONObject obj = new JSONObject();
        try{
            Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
            if(optionalUser.isPresent()){
                User User = optionalUser.get();
                obj.put("success",true);
                obj.put("user_id",User.getId());
                obj.put("username",User.getName());
                obj.put("profileImage",User.getPicture());
            }
            return obj;
        }catch (NullPointerException e){
            obj.put("success",false);
            return obj;
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
}
