package Matching.SouP.repository;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.project.ProjectInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostsRepository extends PagingAndSortingRepository<Post, Long> {

    @Query("select p from Post p order by p.ipDate DESC")
    List<Post> findAllDesc();
}
