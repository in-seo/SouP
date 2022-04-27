package Matching.SouP.repository;

import Matching.SouP.domain.posts.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface PostsRepository extends PagingAndSortingRepository<Post, Long> {

    @Query("select p from Post p order by p.date DESC")
    Page<Post> findAllDesc(Pageable pageable);

    @Query("select p from Post p order by p.date DESC")
    List<Post> findAllDesc();

//    @Query("select p from Post p join fetch p.projectConnectList join fetch p.questionList where p.stack like '%spring%' order by p.date desc") //방금 올라온 따끈따근한 스프는?
//    Post findRecentPost();


    @Query("select p from Post p where p.date>:date order by p.views desc")
    List<Post> findAllNDaysBefore(String date);




}
