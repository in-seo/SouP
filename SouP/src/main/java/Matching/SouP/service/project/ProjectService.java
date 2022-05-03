package Matching.SouP.service.project;


import Matching.SouP.crawler.ConvertToPost;
import Matching.SouP.crawler.CrawlerService;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.posts.Source;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.favForm;
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
import java.util.LinkedHashMap;
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
        String talk = "";  StringBuilder stack = new StringBuilder();
        talk = parseTalk(str, talk);     stack = parseStack(pForm.getTitle(),str,stack);
        Post post = new Post(soupId++,pForm.getTitle(),pForm.getContent().toString(),user.getName(), LocalDateTime.now().toString().substring(0,19),"미정",stack.toString(),1,talk, Source.SOUP);
        post.setParse(str.substring(0,199));
        convertToPost.soup(post, user);//post형태로 회원과 연결 및 저장

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
        List<ProjectConnect> projectList = projectConnectRepository.findByPostId(form.getId());
        for (ProjectConnect connect : projectList) {
            if(connect.getUser().getId()==user.getId()){
                log.warn("이미 누른 회원입니다.");
                isfav=true;
                break;
            }
        }
        Post post = postsRepository.findById(form.getId()).orElseThrow();
        if (form.isMode() && !isfav){
            post.plusFav();
            ProjectConnect connect = ProjectConnect.createConnect(post, user);
            user.getProjectConnectList().add(connect); //스크랩!
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

//    @Transactional
//    public Long addQuestion(Long projectId,Long userId, Project_Question question){  //댓글추가메서드./  생성은따로만들예정
//        ProjectInfo projectInfo = projectInfoRepository.findById(projectId).get();
//        User writer = userRepository.findById(userId).get();
//        question.setProjectInfo(projectInfo);  //어떤 프로젝트에 대한 질문인지?
//        question.setUser(writer);  // 어떤 사람이 쓴 댓글인지?
//        questionRepository.save(question);
//        return question.getId();
//    }

//    @Transactional
//    public ProjectInfo editProject(@LoginUser User user,EditForm editForm, Long id ){
//        Optional<ProjectInfo> byId = projectInfoRepository.findById(id);
//        if(byId.isPresent()) {
//            ProjectInfo edit = byId.get();
//            ProjectConnect connect = projectConnectRepository.findProjectConnectByProjectInfo(edit.getId());
//
//            if(connect.getUser().getEmail()!=user.getEmail())
//                return edit;  //수정 불가
//
//            edit.setProjectName(editForm.getProjectName());edit.setText(editForm.getText());
//            edit.setStack(editForm.getStack()); edit.setLink(editForm.getData());
//            return edit;
//        } else{
//            log.error("해당 게시글이 없습니다. id="+ id);
//        }
//        return byId.get();
//    }
//
//    @Transactional
//    public void deleteProject(Long id) {
//        Optional<ProjectInfo> del1 = projectInfoRepository.findById(id);
//        Optional<ProjectConnect> del2 = projectConnectRepository.findById(id);
//        if(del1.isPresent() && del2.isPresent()){
//            ProjectInfo delete1 = del1.get();
//            ProjectConnect delete2 = del2.get();
//            projectInfoRepository.delete(delete1);
//            projectConnectRepository.delete(delete2);
//        }
//        else
//            log.error("존재하지 않는 게시글");
//    }

//    @Transactional
//    public JSONObject fav(User user, @RequestBody favForm form){   // 좋아요
//        JSONObject obj = new JSONObject();
//        boolean isfav=false;
//        ProjectConnect author = projectConnectRepository.findByPostId(form.getId()).get(0);
//        Set<ProjectConnect> userProjectConnectList = user.getProjectConnectList();
//        for (ProjectConnect connect : userProjectConnectList) {
//            if(connect.getUser().getId()==author.getUser().getId()){
//                log.warn("이미 누른 회원이거나 작성자입니다.");
//                obj.put("success", false);
//                obj.put("isfav", true);
//                return obj;
//            }
//        }
//        List<ProjectConnect> projectList = projectConnectRepository.findByPostId(form.getId());
//        for (ProjectConnect connect : projectList) {
//            if(connect.getUser().getId()==user.getId()){
//                log.warn("이미 누른 회원입니다.");
//                isfav=true;
//                break;
//            }
//        }
//        Post post = postsRepository.findById(form.getId()).orElseThrow();
//        if (form.isMode() && !isfav){
//            post.plusFav();
//            ProjectConnect connect = ProjectConnect.createConnect(post, user);
//            projectConnectRepository.save(connect);
//            isfav=true;
//            obj.put("success",true);
//        }
//        else if(!form.isMode() && isfav){
//            post.minusFav();
//            for (ProjectConnect connect : projectList) {
//                if(connect.getUser().getId()==user.getId()){
//                    projectConnectRepository.delete(connect);  //작성자가 끊어지면 안되는데 이거 고려해보자..
//                    isfav=false;
//                    break;
//                }
//            }
//            obj.put("success",true);
//        }
//        else
//            obj.put("success",false);
//        obj.put("isfav",isfav);
//        return obj;
//    }
}
