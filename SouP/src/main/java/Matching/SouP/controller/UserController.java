package Matching.SouP.controller;

import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.controller.exception.ErrorResponse;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.UserForm;
import Matching.SouP.dto.project.ShowForm;
import Matching.SouP.repository.UserRepository;
import Matching.SouP.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
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
                obj.put("userName",User.getNickName());
//                obj.put("profileImage",User.getPicture());
            }
            return obj;
        }catch (NullPointerException e){
            obj.put("success",false);
            return obj;
        }
    }


    @GetMapping("/myfav")
    @ApiOperation(value = "내 스크랩 글 조회")
    public List<ShowForm> showMyFav(@LoginUser SessionUser user){
        User User = userRepository.findByEmailFetchPC(user.getEmail()).orElseThrow();
        return userService.showFav(User);
    }

    @GetMapping("/mypage")
    @ApiOperation(value = "내 정보 조회")
    public JSONObject showMyInfo(@LoginUser SessionUser user){
        JSONObject obj = new JSONObject();
        try{
            Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
            if(optionalUser.isPresent()){
                User User = optionalUser.get();
                obj.put("success",true);
                obj.put("user_id",User.getId());
                obj.put("email", User.getEmail());
                obj.put("userName",User.getNickName());
                obj.put("origin",User.getOrigin());
            }
            return obj;
        }catch (NullPointerException e){
            obj.put("success",false);
            return obj;
        }
    }

    @PostMapping("/nickname")
    @Transactional
    @ApiOperation(value = "닉네임 변경")
    public JSONObject changeNick(@LoginUser SessionUser user, @RequestBody UserForm userForm){
        JSONObject obj = new JSONObject();
        try{
            Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
            if(optionalUser.isPresent()){
                User User = optionalUser.get();
                if(userForm.isMode()){
                    User.changeName(userForm.getUserName());
                    obj.put("success",true);
                }
                else
                    obj.put("success",false);
                log.info("[닉네임 변경] user ID = {}, {}님이 닉네임 {}로 변경함",User.getId(),User.getName(),User.getNickName());
            }
            return obj;
        }catch (NullPointerException e){
            obj.put("success",false);
            return obj;
        }
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleException1() {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, "존재하지 않는 회원이거나 null 값 조회중입니다.");
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleException2() {
        return ErrorResponse.of(HttpStatus.NOT_FOUND, "존재하지 않는 회원이거나 존재하지 않는 글에 요청을 합니다.");
    }

    //    @PostMapping("/profile")  //Post요청 시
//    @Transactional
//    public String profile(UserForm userForm) {
//        User user = userRepository.findById(userForm.getId()).get();
//        user.updateProfile(userForm);  //이메일이 있다면 정식멤버 승인.
//        sessionReset(user); //회원등급 GUEST -> USER로 변환
//        return "redirect:/";
//    }

//    public void sessionReset(User user){
//        /**
//         * 지리는 코드다 시바;;;
//         */
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
//        for (GrantedAuthority updatedAuthority : updatedAuthorities) {
//            System.out.println(updatedAuthority.getAuthority());
//        }
//        updatedAuthorities.add(new SimpleGrantedAuthority(user.getRoleKey())); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
//    }
}
