package Matching.SouP.service;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostsRepository postsRepository;

    public List<Post> findAllDesc(){
        List<Post> posts = postsRepository.findAllDesc();
        return posts;
    }
}
