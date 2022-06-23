package Matching.SouP.service;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    public List<Post> showPost(User user){
        return user.getPostList();
    }

    public List<ProjectConnect> showFav(User user){
        return user.getProjectConnectList();
    }

}
