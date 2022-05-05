package Matching.SouP.service;

import Matching.SouP.crawler.okky.Okky;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.posts.Source;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.project.ShowForm;
import Matching.SouP.repository.PostsRepository;
import Matching.SouP.repository.ProjectConnectRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService{
    private final ProjectConnectRepository projectConnectRepository;
    private final PostsRepository postsRepository;

    public PageImpl<ShowForm> projectListForUser(User user, List<String> stacks, Pageable pageable){
        Page<Post> projectList = postsRepository.findAllDesc(pageable);
        List<ShowForm> showList = new ArrayList<>();

        projectList = filter(stacks, pageable, projectList);  //파라미터 입력받았을 경우

        for (Post post : projectList.getContent()) {
            boolean br = true;
            boolean isStack=true;
            for (String stack : stacks) {
                if(!post.getStack().contains(stack))
                    isStack=false;
            }
            if(isStack){
                ShowForm showForm = new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav());
                if(post.getSource()==Source.SOUP)
                    showForm.setContent(post.getParse());
                List<ProjectConnect> projectConnectList = projectConnectRepository.findByPostId(post.getId());
                for (ProjectConnect projectConnect : projectConnectList) {
                    if(projectConnect.getUser().getId()== user.getId()) {
                        showForm.setIsfav(true);
                        br=false;
                    }
                }
                if(br)
                    showForm.setIsfav(false);
                showList.add(showForm);
            }
        }
        return new PageImpl<>(showList, pageable, projectList.getTotalElements());
    }

    public PageImpl<ShowForm> projectListForGuest(List<String> stacks, Pageable pageable){
        List<ShowForm> showList = new ArrayList<>();
        Page<Post> projectList = postsRepository.findAllDesc(pageable);  //일반적 조회시

        projectList = filter(stacks, pageable, projectList);

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

    public JSONObject showProject(Long id) throws ParseException {
        Optional<Post> Opost = postsRepository.findById(id);
        JSONObject obj = new JSONObject();
        if(Opost.isPresent()){
            Post post = Opost.get();
            obj.put("title",post.getPostName());
            if(post.getSource()==Source.SOUP){
                JSONParser parser = new JSONParser();
                JSONObject parse = (JSONObject) parser.parse(post.getContent());
                obj.put("type","prosemirror");
                obj.put("content",parse);
            }
            else{
                obj.put("type","string");
                obj.put("content",post.getContent());
            }
            obj.put("source",post.getSource());
            obj.put("stacks",post.getStack());
            obj.put("url",post.getLink());
        }
        return obj;
    }


    public List<ShowForm> findRecentPost(){
        List<Post> projectList = postsRepository.findTop3OrderByDateDesc();
        List<ShowForm> recentPost = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Post post = projectList.get(i);
            if(post.getSource()==Source.SOUP)
                recentPost.add(new ShowForm(post.getId(),post.getPostName(),post.getParse(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav()));
            else
                recentPost.add(new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav()));
        }
        return recentPost;
    }

    public List<ShowForm> findHotPost(long n){
        List<Post> projectList = postsRepository.findAllNDaysBefore(LocalDateTime.now().minusDays(3).toString().substring(0,18));
        List<ShowForm> hotList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Post post = projectList.get(i);
            hotList.add(new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav()));
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
//    @Transactional(readOnly = true)
//    public PageImpl<ShowForm> showProjectForGuest(Pageable pageable){
//        List<ShowForm> showList = new ArrayList<>();
//        Page<Post> projectList = postsRepository.findAllDesc(pageable);
//        for (Post post : projectList.getContent()) {
//            Long postId = post.getId();
//            ShowForm showForm = new ShowForm(postId,post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav());
//            showList.add(showForm);
//        }
//        return new PageImpl<>(showList, pageable, projectList.getTotalElements());
//    }

    private Page<Post> filter(List<String> stacks, Pageable pageable, Page<Post> projectList) {
        if (stacks.size() == 1)
            projectList = postsRepository.findBy1StacksDesc(pageable, stacks.get(0));
        else if (stacks.size() == 2)
            projectList = postsRepository.findBy2StacksDesc(pageable, stacks.get(0), stacks.get(1));
        else if (stacks.size() == 3)
            projectList = postsRepository.findBy3StacksDesc(pageable, stacks.get(0), stacks.get(1), stacks.get(2));
        return projectList;
    }
}
