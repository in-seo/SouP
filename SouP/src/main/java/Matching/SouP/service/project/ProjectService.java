package Matching.SouP.service.project;


import Matching.SouP.config.auth.LoginUser;
import Matching.SouP.crawler.ConvertToPost;
import Matching.SouP.crawler.CrawlerService;
import Matching.SouP.domain.posts.LoungeConnect;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.posts.Source;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.project.Project_Question;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.favForm;
import Matching.SouP.dto.project.EditForm;
import Matching.SouP.dto.project.PostForm;
import Matching.SouP.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService  extends CrawlerService {
    private final ProjectConnectRepository projectConnectRepository;
    private final PostsRepository postsRepository;
    private final QuestionRepository questionRepository;
    private final ConvertToPost convertToPost;
    private static Long soupId=1L;


    @Transactional
    public Long tempSave(PostForm pForm, User user){  //프로젝트정보 임시저장, 사람과 연결 전
        String talk = "";  StringBuilder stack = new StringBuilder();
        talk = parseTalk(pForm.getContent(), talk);
        stack = parseStack(pForm.getPostName(),pForm.getContent(),stack);
        Post post = new Post(soupId++,pForm.getPostName(),pForm.getContent(),user.getName(), LocalDateTime.now().toString().substring(0,18),"미정",stack.toString(),1,talk, Source.SOUP);
        convertToPost.soup(post);
        ProjectConnect connect = new ProjectConnect();
        connect.createConnect(post,user);
        projectConnectRepository.save(connect);  //회원 연결
        return post.getId();
    }
    @Transactional
    public JSONObject fav(User user, @RequestBody favForm form){   // 좋아요
        JSONObject obj = new JSONObject();
        boolean userCheck=true;
        List<ProjectConnect> postList = projectConnectRepository.findByPostId(form.getId());
        for (ProjectConnect connect : postList) {
            if(connect.getUser().getId()==user.getId()){
                log.warn("이미 누른 회원이거나 작성자입니다.");
                userCheck=false;
                break;
            }
        }
        Post post = postsRepository.findById(form.getId()).orElseThrow();
        if (form.isMode() && userCheck){
            post.plusFav();
            ProjectConnect connect = new ProjectConnect();
            connect.createConnect(post,user);
            projectConnectRepository.save(connect);
            obj.put("success",true);
        }
        else if(!form.isMode() && !userCheck){
            post.minusFav();
            for (ProjectConnect connect : postList) {
                if(connect.getUser().getId()==user.getId()){
                    projectConnectRepository.delete(connect);  //작성자가 끊어지면 안되는데 이거 고려해보자..
                    break;
                }
            }
            obj.put("success",true);
        }
        else
            obj.put("success",false);
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


}
