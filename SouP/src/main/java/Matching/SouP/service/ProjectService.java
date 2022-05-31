package Matching.SouP.service;


import Matching.SouP.crawler.ConvertToPost;
import Matching.SouP.crawler.CrawlerService;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.posts.Source;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.favForm;
import Matching.SouP.dto.project.EditForm;
import Matching.SouP.dto.project.PostForm;
import Matching.SouP.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService  extends CrawlerService {
    private final ProjectConnectRepository projectConnectRepository;
    private final PostsRepository postsRepository;
    private final ConvertToPost convertToPost;
    private static Long soupId=1L;


    @Transactional
    public JSONObject tempSave(PostForm pForm, User user) throws ParseException {  //프로젝트정보 임시저장, 사람과 연결 전
        JSONObject obj = new JSONObject();
        JSONParser parser = new JSONParser();
        JSONObject content = (JSONObject)parser.parse(String.valueOf(pForm.getContent()));
        String temp="";
        String str= parseString(content,temp);
        String talk = "";
        talk = parseTalk(str, talk);     StringBuilder stack = parseStack(pForm.getTitle(),str);
        Post post = new Post(soupId++,pForm.getTitle(),pForm.getContent().toString(),user.getName(), LocalDateTime.now().toString().substring(0,19),"",stack.toString(),5,talk, Source.SOUP);
        Post soup = convertToPost.soup(post, user);//post형태로 회원과 연결 및 저장
        obj.put("id",soup.getId());
        obj.put("success", true);
        return obj;
    }

    private String parseString(JSONObject obj,String str){
        if(obj.containsKey("text")){
            str+=obj.get("text").toString();
        }
        if(obj.containsKey("content")){
            JSONArray content = (JSONArray) obj.get("content");
            String s = "";
            for (int i = 0; i < content.size(); i++) {
                JSONObject jsonObject = (JSONObject) content.get(i);
                s += parseString(jsonObject,str);
            }
            str+=s;
        }
        return str;
    }


    @Transactional
    public JSONObject fav(User user, @RequestBody favForm form){   // 좋아요
        JSONObject obj = new JSONObject();
        boolean isfav=false;
        List<ProjectConnect> projectList = new ArrayList<>();
        if(user.getProjectConnectList().size()!=0){
            projectList = projectConnectRepository.findByPostId(form.getId());
            for (ProjectConnect connect : projectList) {
                if(connect.getUser().getId()==user.getId()){
                    isfav=true;
                    break;
                }
            }
        }

        Post post = postsRepository.findById(form.getId()).orElseThrow();
        if (form.isMode() && !isfav){
            post.plusFav();
            ProjectConnect connect = ProjectConnect.createConnect(post, user);
            projectConnectRepository.save(connect);
            isfav=true;
            obj.put("success",true);
        }
        else if(!form.isMode() && isfav){
            post.minusFav();
            for (ProjectConnect connect : projectList) {
                if(connect.getUser().getId()==user.getId()){
                    projectConnectRepository.delete(connect);
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

    @Transactional
    public JSONObject editProject(User user, EditForm form){
        JSONObject obj = new JSONObject();
        Post post = postsRepository.findById(form.getId()).orElseThrow();
        if(post.getUser().getId()==user.getId()){
            post.edit(form.getTitle(),form.getContent().toString());
            obj.put("success", true);
        }
        else
            obj.put("success", false);
        return obj;
    }

    @Transactional
    public JSONObject deleteProject(User user, Long id) {
        JSONObject obj = new JSONObject();
        Post post = postsRepository.findById(id).orElseThrow();
        if(post.getUser().getId()==user.getId()){
            postsRepository.delete(post);
            obj.put("success", true);
        }
        else
            obj.put("success", false);
        return obj;
    }
}
