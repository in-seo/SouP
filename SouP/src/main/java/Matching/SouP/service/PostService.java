package Matching.SouP.service;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import java.time.LocalDateTime;
import java.util.*;

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

    public Post findRecentPost(){return postsRepository.findRecentPost(); }

    public List<Post> findAllNDaysBefore(long n){
        List<Post> list = postsRepository.findAllNDaysBefore(LocalDateTime.now().minusDays(n).toString().substring(0,18));
        List<Post> hotList = new ArrayList<>(Arrays.asList(list.get(0),list.get(1),list.get(2)));
        return hotList;
    }

}
