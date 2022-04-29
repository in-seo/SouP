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
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class LoungeService {
    private final LoungeRepository loungeRepository;
    private final LoungeConnectRepository loungeConnectRepository;

    @Transactional(readOnly = true)
    public JSONArray showLoungeForUser(User user){   //라운지 보여주기
        List<LoungeConnect> loungeList = loungeConnectRepository.findAllDesc();
        JSONArray arr = new JSONArray();
        for (LoungeConnect connect : loungeList) {
            boolean br = true;
            JSONObject obj=new JSONObject();
            obj.put("user_id",connect.getUser().getId());
            obj.put("user",connect.getUser().getName());
            obj.put("picture",connect.getUser().getPicture());
            obj.put("content",connect.getLounge().getContent());
            obj.put("date",connect.getCreatedDate().toString());
            obj.put("fav",connect.getLounge().getFav());
            Long loungeId = connect.getLounge().getId();
            obj.put("lounge_id",loungeId);
//            if(connect.getUser().getId()==user.getId()){
//                obj.put("isfav",true);
//            }
            List<LoungeConnect> byLoungeId = loungeConnectRepository.findByLoungeId(loungeId);
            for (LoungeConnect loungeConnect : byLoungeId) {
                if(loungeConnect.getUser().getId()== user.getId()) {
                    obj.put("isfav", true);
                    br=false;
                }
            }
            if(br)
                obj.put("isfav",false);
            arr.add(obj);
        }
        return arr;
    }
    @Transactional(readOnly = true)
    public JSONArray showLoungeForGuest(){   //라운지 보여주기
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
            Long loungeId = connect.getLounge().getId();
            obj.put("lounge_id",loungeId);
            obj.put("isfav",false);
            arr.add(obj);
        }
        return arr;
    }

    @Transactional
    public JSONObject addLounge(User user, @RequestBody LoungeForm form){  //라운지에 글 게시시 post로 요청받고 하는 일
            JSONObject obj = new JSONObject();

            Lounge lounge = new Lounge(form.getContent());
            loungeRepository.save(lounge);
            LoungeConnect connect = LoungeConnect.createConnect(lounge, user);  //라운지와 유저 연결
            user.getLoungeConnectList().add(connect);  //유저에게 작성자 권한 부여.
            loungeConnectRepository.save(connect);
            obj.put("success",true);

            return obj;
    }

    @Transactional
    public JSONObject fav(User user, @RequestBody favForm form){   // 좋아요
        JSONObject obj = new JSONObject();
        boolean isfav=false;

        LoungeConnect author = loungeConnectRepository.findByLoungeId(form.getId()).get(0);  //첫번쨰 사람이 작성자이다.
        List<LoungeConnect> userLoungeConnectList = user.getLoungeConnectList();
        for (LoungeConnect connect : userLoungeConnectList) {
            if(connect.getUser().getId()==author.getUser().getId()) {
                log.warn("글 작성자입니다.");
                obj.put("success", false);
                obj.put("isfav", true);
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
            lounge.getLoungeConnectList().add(connect);
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
