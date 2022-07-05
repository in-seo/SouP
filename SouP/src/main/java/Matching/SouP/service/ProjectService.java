package Matching.SouP.service;


import Matching.SouP.config.MyOkHttpClient;
import Matching.SouP.crawler.ConvertToPost;
import Matching.SouP.crawler.CrawlerService;
import Matching.SouP.crawler.okky.Okky;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.posts.Source;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.favForm;
import Matching.SouP.dto.project.EditForm;
import Matching.SouP.dto.project.PostForm;
import Matching.SouP.dto.project.ShowForm;
import Matching.SouP.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    @CacheEvict(value = { "front", "featured" }, allEntries = true)
    public JSONObject tempSave(PostForm pForm, User user) throws ParseException {  //프로젝트정보 임시저장, 사람과 연결 전
        JSONObject obj = new JSONObject();
        JSONParser parser = new JSONParser();
        JSONObject content = (JSONObject)parser.parse(String.valueOf(pForm.getContent()));
        String temp="";
        String prosemirror= parseString(content,temp);
        String talk = "";
        talk = parseTalk(prosemirror, talk);     StringBuilder stack = parseStack(pForm.getTitle(),prosemirror);
        Post post = new Post(soupId++,pForm.getTitle(),pForm.getContent().toString(),user.getNickName(), LocalDateTime.now().toString().substring(0,19),"",stack.toString(),5,talk, Source.SOUP);
        post.setProsemirror(prosemirror);
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
                if(connect.getUser().getId().equals(user.getId())){
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
                if(connect.getUser().getId().equals(user.getId())){
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
        if(post.getUser().getId().equals(user.getId())){
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
        if(post.getUser().getId().equals(user.getId())){
            postsRepository.delete(post);
            obj.put("success", true);
        }
        else
            obj.put("success", false);
        return obj;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowForm> findAllDesc(Source source) {
        List<Post> postList = postsRepository.findTop8BySourceOrderByDateDesc(source);
        List<ShowForm> showList = new ArrayList<>();
        for (Post post : postList) {
            ShowForm showForm = new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(), Source.OKKY,0);
            showList.add(showForm);
        }
        return showList;
    }

    public String makeTemplate(Source source, String link, String stack, String email, String talk){
        String start = "안녕하세요 "+ source +"의 "+link+ " 보고 연락 드렸습니다.\n";
        String middle = " 모집중이신 "+stack+"을 이용한 프로젝트/스터디에 관심이 있고 [           ] 정도 다뤄봤으며, [          ]와 같은 구현 경험이 있습니다.\n";
        String end = " 자세한 내용은 ['깃허브주소']를 참고해 주시거나 "+email+"으로 연락주세요. 카톡도 가능합니다 :) !! \n\n지원 오픈카톡 --> "+ talk;
        return start+middle+end;
    }
}
