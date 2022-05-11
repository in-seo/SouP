package Matching.SouP.service;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.posts.Source;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.project.DetailForm;
import Matching.SouP.dto.project.ShowForm;
import Matching.SouP.repository.PostsRepository;
import Matching.SouP.repository.ProjectConnectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService{
    private final ProjectConnectRepository projectConnectRepository;
    private final PostsRepository postsRepository;

    public PageImpl<ShowForm> projectListForUser(User user, List<String> stacks, Pageable pageable){
        Page<Post> projectList;
        if(stacks.size()==0)
            projectList = postsRepository.findAllDesc(pageable);
        else
            projectList = filter(stacks, pageable);  //파라미터 입력받았을 경우
        List<ShowForm> showList = new ArrayList<>();
        for (Post post : projectList.getContent()) {
            boolean isStack=true;
            for (String stack : stacks) {
                if (!post.getStack().contains(stack))
                    isStack = false;
            }
            if(isStack){
                ShowForm showForm = new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav());
                if(post.getSource()==Source.SOUP)
                    showForm.setContent(post.getParse());
                if(user.getProjectConnectList().size()!=0){
                    List<ProjectConnect> projectConnectList = projectConnectRepository.findByPostId(post.getId()); //바꾸자
                    for (ProjectConnect projectConnect : projectConnectList) {
                        if(projectConnect.getUser().getId()== user.getId()) {
                            showForm.setIsfav(true);
                            break;
                        }
                    }
                }
                showList.add(showForm);
            }
        }
        return new PageImpl<>(showList, pageable, projectList.getTotalElements());
    }

    public PageImpl<ShowForm> projectListForGuest(List<String> stacks, Pageable pageable){
        Page<Post> projectList;
        if(stacks.size()==0)
            projectList = postsRepository.findAllDesc(pageable);
        else
            projectList = filter(stacks, pageable);  //파라미터 입력받았을 경우
        List<ShowForm> showList = new ArrayList<>();

        for (Post post : projectList.getContent()) {
            boolean isStack=true;
            for (String stack : stacks) {
                if(!post.getStack().contains(stack))
                    isStack=false;
            }
            if(isStack){
                ShowForm showForm = new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav());
                if(post.getSource()==Source.SOUP)
                    showForm.setContent(post.getParse());
                showList.add(showForm);
            }
        }
        return new PageImpl<>(showList, pageable, projectList.getTotalElements());
    }

    public DetailForm showProject(Long id,User user) throws ParseException {
        Optional<Post> Opost = postsRepository.findById(id);
        DetailForm form = null;
        if(Opost.isPresent()){
            form = getDetailForm(Opost);
            for (ProjectConnect connect : user.getProjectConnectList()) {
                if(connect.getPost().getId()==id)
                    form.setIsfav(true);
            }
        }
        return form;
    }
    public DetailForm showProject(Long id) throws ParseException {
        Optional<Post> Opost = postsRepository.findById(id);
        DetailForm form = null;
        if(Opost.isPresent()){
            form = getDetailForm(Opost);
        }
        return form;
    }

    public List<ShowForm> findRecentPost(){
        List<Post> projectList = postsRepository.findTop3ByOrderByDateDesc();
        List<ShowForm> recentPost = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Post post = projectList.get(i);
            ShowForm showForm;
            if(post.getSource()==Source.SOUP)
                showForm = new ShowForm(post.getId(), post.getParse(), post.getContent(), post.getUserName(), post.getDate(), post.getLink(), post.getStack(),post.getViews(), post.getTalk(), post.getSource(), post.getFav());
            else
                showForm = new ShowForm(post.getId(), post.getPostName(), post.getContent(), post.getUserName(), post.getDate(), post.getLink(), post.getStack(), post.getViews(), post.getTalk(), post.getSource(), post.getFav());
            recentPost.add(showForm);
        }
        return recentPost;
    }


    public List<ShowForm> findHotPost(long n){
        List<Post> projectList = postsRepository.findAllNDaysBefore(LocalDateTime.now().minusDays(3).toString().substring(0,18));
        List<ShowForm> hotList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Post post = projectList.get(i);
            ShowForm showForm = new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav());
            hotList.add(showForm);
        }
        return hotList;
    }


    public List<ShowForm> findAllDesc() {
        List<Post> soupList = postsRepository.findTop8BySourceOrderByDateDesc(Source.SOUP);
        List<ShowForm> showList = new ArrayList<>();
        for (Post soup : soupList) {
            ShowForm showForm = new ShowForm(soup.getId(),soup.getPostName(),soup.getParse(),soup.getUserName(),soup.getDate(),soup.getLink(),soup.getStack(),soup.getViews(),soup.getTalk(), Source.SOUP,0);
            showList.add(showForm);
        }
        return showList;
    }

    private Page<Post> filter(List<String> stacks, Pageable pageable) {
        if (stacks.size() == 1)
            return postsRepository.findBy1StacksDesc(pageable, stacks.get(0));
        else if (stacks.size() == 2)
            return postsRepository.findBy2StacksDesc(pageable, stacks.get(0), stacks.get(1));
        else if (stacks.size() == 3)
            return postsRepository.findBy3StacksDesc(pageable, stacks.get(0), stacks.get(1), stacks.get(2));
        else
            return postsRepository.findAllDesc(pageable);
    }


//    public PageImpl<ShowForm> projectListForUser(User user, List<String> stacks, Pageable pageable){
//        Page<Post> projectList = postsRepository.findAllDesc(pageable);
//        List<ShowForm> showList = new ArrayList<>();
//
//        projectList = filter(stacks, pageable, projectList);  //파라미터 입력받았을 경우
//
//        for (Post post : projectList.getContent()) {
//            boolean br = true;
//            boolean isStack=true;
//            for (String stack : stacks) {
//                if (!post.getStack().contains(stack))
//                    isStack = false;
//            }
//            if(isStack){
//                ShowForm showForm = new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav());
//                if(post.getSource()==Source.SOUP)
//                    showForm.setContent(post.getParse());
//                List<ProjectConnect> projectConnectList = projectConnectRepository.findByPostId(post.getId()); //바꾸자
//                for (ProjectConnect projectConnect : projectConnectList) {
//                    if(projectConnect.getUser().getId()== user.getId()) {
//                        showForm.setIsfav(true);
//                        br=false;
//                    }
//                }
//                if(br)
//                    showForm.setIsfav(false);
//                showList.add(showForm);
//            }
//        }
//        return new PageImpl<>(showList, pageable, projectList.getTotalElements());
//    }
    private DetailForm getDetailForm(Optional<Post> opost) throws ParseException {
        DetailForm form;
        Post post = opost.get();
        if(post.getSource()== Source.SOUP){
            JSONParser parser = new JSONParser();
            JSONObject parse = (JSONObject) parser.parse(post.getContent());
            form = new DetailForm(post.getId(), post.getPostName(), parse.toString(), post.getUserName(), post.getDate(), post.getLink(), post.getStack(), post.getViews(), post.getTalk(), post.getSource(), post.getFav());
            form.setType("prosemirror");
        }
        else{
            form = new DetailForm(post.getId(), post.getPostName(), post.getContent(), post.getUserName(), post.getDate(), post.getLink(), post.getStack(), post.getViews(), post.getTalk(), post.getSource(), post.getFav());
            form.setType("string");
        }
        return form;
    }
}
