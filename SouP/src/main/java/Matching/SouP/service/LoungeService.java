package Matching.SouP.service;

import Matching.SouP.domain.posts.Lounge;
import Matching.SouP.domain.posts.LoungeConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.LoungeForm;
import Matching.SouP.dto.favForm;
import Matching.SouP.repository.LoungeConnectRepository;
import Matching.SouP.repository.LoungeRepository;
import Matching.SouP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoungeService {
    private final LoungeRepository loungeRepository;
    private final LoungeConnectRepository loungeConnectRepository;
    private final UserRepository userRepository;

    public JSONArray showLounge(){   //라운지 보여주기
        List<LoungeConnect> loungeList = loungeConnectRepository.findAllDesc();
        JSONArray arr = new JSONArray();
        for (LoungeConnect lounge : loungeList) {
            JSONObject obj=new JSONObject();
            obj.put("user",lounge.getUser().getName());
            obj.put("picture",lounge.getUser().getPicture());
            obj.put("content",lounge.getLounge().getContent());
            obj.put("date",lounge.getCreatedDate().toString());
            obj.put("fav",lounge.getLounge().getFav());
            arr.add(obj);
        }
        return arr;
    }

    @PostMapping("/lounge/add")
    public JSONObject addLounge(User user, @RequestBody LoungeForm form){  //라운지에 글 게시시 post로 요청받고 하는 일
            JSONObject obj = new JSONObject();
            Optional<User> User = userRepository.findByEmail(user.getEmail());
            if(User.isPresent()){
                User currentUser = User.get();
                Lounge lounge = new Lounge(form.getContent());
                loungeRepository.save(lounge);
                LoungeConnect connect = LoungeConnect.createConnect(lounge, currentUser);
                loungeConnectRepository.save(connect);
                obj.put("success",true);
            }
            else{
                log.warn("라운지는 회원가입 후 이용가능");
                obj.put("success",false);
            }
            return obj;
    }

    public JSONObject fav(User user, @RequestBody favForm form){   // 좋아요
        JSONObject obj = new JSONObject();
        boolean userCheck=true;
        List<LoungeConnect> loungeList = loungeConnectRepository.findByLoungeId(form.getId());
        for (LoungeConnect connect : loungeList) {
            if(connect.getUser().getId()==user.getId()){
                log.warn("이미 누른 회원이거나 작성자입니다.");
                userCheck=false;
                break;
            }
        }
        Lounge lounge = loungeRepository.findById(form.getId()).orElseThrow();
        if (form.isMode() && userCheck){
            lounge.plusFav();
            LoungeConnect connect = LoungeConnect.createConnect(lounge, user);
            loungeConnectRepository.save(connect);
            obj.put("success",true);
        }
        else if(!form.isMode() && !userCheck){
            lounge.minusFav();
            for (LoungeConnect connect : loungeList) {
                if(connect.getUser().getId()==user.getId()){
                    loungeConnectRepository.delete(connect);
                    break;
                }
            }

            obj.put("success",true);
        }
        else
            obj.put("success",false);
        return obj;
    }
}
