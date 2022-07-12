package Matching.SouP.service;

import Matching.SouP.domain.post.Lounge;
import Matching.SouP.domain.post.LoungeConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.LoungeForm;
import Matching.SouP.dto.FavForm;
import Matching.SouP.repository.LoungeConnectRepository;
import Matching.SouP.repository.LoungeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class LoungeService {
    private final LoungeRepository loungeRepository;
    private final LoungeConnectRepository loungeConnectRepository;

    @Transactional(readOnly = true)
    public JSONArray showLoungeForUser(User user){   //라운지 보여주기
        List<Lounge> loungeList = loungeRepository.findAllDesc();
        JSONArray arr = new JSONArray();
        for (Lounge lounge : loungeList) {
            boolean br = true;
            JSONObject obj = makeLoungeForm(lounge);
            if(user.getLoungeConnectList().size()!=0){
                List<LoungeConnect> byLoungeId = loungeConnectRepository.findByLoungeId(lounge.getId());
                for (LoungeConnect loungeConnect : byLoungeId) {
                    if(loungeConnect.getUser().getId().equals(user.getId())) {
                        obj.put("isfav", true);
                        br=false;
                    }
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
        List<Lounge> loungeList = loungeRepository.findAllDesc();
        JSONArray arr = new JSONArray();
        for (Lounge lounge : loungeList) {
            JSONObject obj = makeLoungeForm(lounge);
            obj.put("isfav",false);
            arr.add(obj);
        }
        return arr;
    }

    @Transactional
    public JSONObject addLounge(User user, @RequestBody LoungeForm form){  //라운지에 글 게시시 post로 요청받고 하는 일
            JSONObject obj = new JSONObject();
            Lounge lounge = new Lounge(form.getContent());
            lounge.setUser(user);
            loungeRepository.save(lounge);
            obj.put("success",true);
            return obj;
    }

    @Transactional
    public JSONObject fav(User user, @RequestBody FavForm form){   // 좋아요
        JSONObject obj = new JSONObject();
        boolean isfav=false;
        List<LoungeConnect> loungeList = new ArrayList<>();
        if(user.getLoungeConnectList().size()!=0){
            loungeList = loungeConnectRepository.findByLoungeId(form.getId());
            for (LoungeConnect connect : loungeList) {
                if(connect.getUser().getId().equals(user.getId())){
                    isfav=true;
                    break;
                }
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
                if(connect.getUser().getId().equals(user.getId())){
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

    private JSONObject makeLoungeForm(Lounge lounge) {
        JSONObject obj=new JSONObject();
        obj.put("user_id",lounge.getUser().getId());
        obj.put("userName",lounge.getUser().getNickName());
        obj.put("picture",lounge.getUser().getPicture());
        obj.put("content",lounge.getContent());
        obj.put("date",lounge.getCreatedDate().toString());
        obj.put("fav",lounge.getFav());
        obj.put("lounge_id",lounge.getId());
        return obj;
    }
}
