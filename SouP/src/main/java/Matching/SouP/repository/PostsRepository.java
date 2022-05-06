package Matching.SouP.repository;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.posts.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface PostsRepository extends PagingAndSortingRepository<Post, Long> {

    @Query("select p from Post p order by p.date DESC")
    Page<Post> findAllDesc(Pageable pageable);

    List<Post> findTop8BySourceOrderByDateDesc(Source SOUP);

    List<Post> findTop3ByOrderByDateDesc();


    @Query("select p from Post p where p.date>:date order by p.views desc")
    List<Post> findAllNDaysBefore(String date);

    @Query("select p from Post p where p.stack like %:stack% order by p.date desc")
    Page<Post> findBy1StacksDesc(Pageable pageable, String stack);

    @Query("select p from Post p where p.stack like %:stack1% and p.stack like %:stack2% order by p.date desc")
    Page<Post> findBy2StacksDesc(Pageable pageable, String stack1, String stack2);

    @Query("select p from Post p where p.stack like %:stack1% and p.stack like %:stack2% and p.stack like %:stack3% order by p.date desc")
    Page<Post> findBy3StacksDesc(Pageable pageable, String stack1, String stack2, String stack3);




}
