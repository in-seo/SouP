package Matching.SouP.service;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.MyFavForm;
import Matching.SouP.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
        private final PostsRepository postsRepository;
//    public List<Post> showPost(User user){
//        return user.getPostList();
//    }

    public List<MyFavForm> showFav(User user){
        JSONObject obj = new JSONObject();
        List<ProjectConnect> projectConnectList = user.getProjectConnectList();
        List<MyFavForm> list = new ArrayList<>();
        for (ProjectConnect pc : projectConnectList) {
            Post post = postsRepository.findById(pc.getPost().getId()).get();
            MyFavForm form = new MyFavForm(post.getPostName(),post.getContent(),post.getUserName(),post.getLink());
            list.add(form);
        }
        return list;
    }

}
