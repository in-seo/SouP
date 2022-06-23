package Matching.SouP.controller;

import Matching.SouP.config.MyOkHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@RestController
public class HomeController {

//    @GetMapping("/kakao")
//    public void kakaoCode(HttpServletResponse response) throws IOException {  //인가코드 받기
//        String str = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+MyOkHttpClient.getAppKey()+"&redirect_uri=http://localhost:8080/oauth&scope=talk_message";
//        response.sendRedirect(str);
//    }
//
//
//    @GetMapping("/oauth")  //인가코드 요청 이후 코드 파싱후 access_token 및 전송 요청
//    public JSONObject sendMessage(@RequestParam("code") String authorizationCode) throws IOException, ParseException {
//        log.warn("sendMessage 진입");
//        JSONObject response = new JSONObject();
//        String accessToken = MyOkHttpClient.getAccessToken(authorizationCode);
//        JSONParser parser = new JSONParser();
//        JSONObject obj = (JSONObject) parser.parse(accessToken);
//        if(obj.containsKey("access_token")){
//            String access_token = obj.get("access_token").toString();
//            response.put("success",MyOkHttpClient.sendTalk(access_token));  //전송
//        }
//        else
//            log.warn("사용자의 access_token을 받아올 수 없습니다.");
//        return response;
//    }
}