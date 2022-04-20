package Matching.SouP.service;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostsRepository postsRepository;

    public Page<Post> findAllDesc(Pageable pageable){
        return postsRepository.findAllDesc(pageable);
    }

    public List<Post> findAllByDesc(){
        return postsRepository.findAllDesc();
    }
}
