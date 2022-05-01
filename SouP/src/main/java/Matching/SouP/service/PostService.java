package Matching.SouP.service;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.project.ShowForm;
import Matching.SouP.repository.PostsRepository;
import Matching.SouP.repository.ProjectConnectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService{
    private final ProjectConnectRepository projectConnectRepository;
    private final PostsRepository postsRepository;

    @Transactional(readOnly = true)
    public PageImpl<ShowForm> showProjectForUser(User user, Pageable pageable){
        Page<Post> projectList = postsRepository.findAllDesc(pageable);
        List<ShowForm> showList = new ArrayList<>();
        for (Post post : projectList.getContent()) {
            boolean br = true;
            Long postId = post.getId();
            ShowForm showForm = new ShowForm(postId,post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav());
            List<ProjectConnect> projectConnectList = projectConnectRepository.findByPostId(postId);
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
        return new PageImpl<>(showList, pageable, projectList.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PageImpl<ShowForm> showProjectForGuest(Pageable pageable){
        List<ShowForm> showList = new ArrayList<>();
        Page<Post> projectList = postsRepository.findAllDesc(pageable);
        for (Post post : projectList.getContent()) {
            Long postId = post.getId();
            ShowForm showForm = new ShowForm(postId,post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav());
            showList.add(showForm);
        }
        return new PageImpl<>(showList, pageable, projectList.getTotalElements());
    }


    public List<ShowForm> findRecentPost(){
        List<Post> projectList = postsRepository.findAllDesc();
        List<ShowForm> recentPost = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Post post = projectList.get(i);
            recentPost.add(new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav()));
        }
        return recentPost;
    }

    public List<ShowForm> findHotPost(long n){
        List<Post> projectList = postsRepository.findAllNDaysBefore(LocalDateTime.now().minusDays(3).toString().substring(0,18));
        List<ShowForm> hotList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Post post = projectList.get(i);
            hotList.add(new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav()));
        }
        return hotList;
    }

}
