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

    public JSONArray showLounge(User user){   //라운지 보여주기
        List<LoungeConnect> loungeList = loungeConnectRepository.findAllDesc();
        JSONArray arr = new JSONArray();
        for (LoungeConnect connect : loungeList) {
            JSONObject obj=new JSONObject();
            obj.put("user_id",connect.getUser().getId());
            obj.put("user",connect.getUser().getName());
            obj.put("picture",connect.getUser().getPicture());
            obj.put("content",connect.getLounge().getContent());
            obj.put("date",connect.getCreatedDate().toString());
            obj.put("fav",connect.getLounge().getFav());
            obj.put("lounge_id",connect.getLounge().getId());
            if(user.getLoungeConnectList().contains(connect)){
                obj.put("isfav",true);
            }
            else
                obj.put("isfav",false);
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
                user.getLoungeConnectList().add(connect);
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
        boolean isfav=false;

        List<LoungeConnect> userLoungeConnectList = user.getLoungeConnectList();
        for (LoungeConnect connect : userLoungeConnectList) {
            if(user.getLoungeConnectList().contains(connect)){
                log.warn("글 작성자입니다.");
                isfav=false;
                obj.put("success",false);
                obj.put("isfav",isfav);
                return obj;
            }
        }

        List<LoungeConnect> loungeList = loungeConnectRepository.findByLoungeId(form.getId());
        for (LoungeConnect connect : loungeList) {
            if(connect.getUser().getId()==user.getId()){
                log.warn("이미 누른 회원입니다.");
                isfav=true;
                break;
            }
        }
        Lounge lounge = loungeRepository.findById(form.getId()).orElseThrow();
        if (form.isMode() && !isfav){
            lounge.plusFav();
            LoungeConnect connect = LoungeConnect.createConnect(lounge, user);
            loungeConnectRepository.save(connect);
            isfav=true;
            obj.put("success",true);
        }
        else if(!form.isMode() && isfav){
            lounge.minusFav();
            for (LoungeConnect connect : loungeList) {
                if(connect.getUser().getId()==user.getId()){
                    loungeConnectRepository.delete(connect);
                    isfav=false;
                    break;
                }
            }
            obj.put("success",true);
        }
        else
            obj.put("success",false);
        obj.put("isfav",isfav);
        return obj;
    }
}
