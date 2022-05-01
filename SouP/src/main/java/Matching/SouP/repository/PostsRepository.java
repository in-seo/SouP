package Matching.SouP.repository;

import Matching.SouP.domain.posts.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface PostsRepository extends PagingAndSortingRepository<Post, Long> {

//    @Query("select p from Post p order by p.date DESC")
//    Page<Post> findAllDesc(Pageable pageable);

    @Query("select p from Post p order by p.date DESC")
    Page<Post> findAllDesc(Pageable pageable);

    @Query("select p from Post p order by p.date DESC")
    List<Post> findAllDesc();

    @Query("select p from Post p where p.date>:date order by p.views desc")
    List<Post> findAllNDaysBefore(String date);




}
