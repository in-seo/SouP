package Matching.SouP.service;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService{

    private final PostsRepository postsRepository;

    public Page<Post> findAllDesc(Pageable pageable){
        return postsRepository.findAllDesc(pageable);
    }

    public List<Post> findAllByDesc(){
        return postsRepository.findAllDesc();
    } //temp

    public List<Post> findRecentPost(){
        List<Post> allDesc = postsRepository.findAllDesc();
        List<Post> recentPost = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            recentPost.add(allDesc.get(i));
        }
        return recentPost;
    }

    public List<Post> findHotPost(long n){
        List<Post> list = postsRepository.findAllNDaysBefore(LocalDateTime.now().minusDays(3).toString().substring(0,18));
        List<Post> hotList = new ArrayList<>(Arrays.asList(list.get(0),list.get(1),list.get(2),list.get(3),list.get(4),list.get(5),list.get(6),list.get(7)));
        return hotList;
    }

}
