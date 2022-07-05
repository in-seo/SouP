package Matching.SouP.repository;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.posts.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostsRepository extends PagingAndSortingRepository<Post, Long> {

    @Query("select p from Post p order by p.date DESC")
    Page<Post> findAllDesc(Pageable pageable);

    List<Post> findTop8BySourceOrderByDateDesc(Source source);

    List<Post> findTop3ByOrderByDateDesc();

    @Query("select p from Post p where p.date>:date order by p.views desc")
    List<Post> findAllNDaysBefore(@Param("date") String date);

    @Query("select p from Post p where p.stack like %:stack% order by p.date desc")
    Page<Post> findBy1StacksDesc(Pageable pageable, @Param("stack") String stack);

    @Query("select p from Post p where p.stack like %:stack1% or p.stack like %:stack2% order by p.date desc")
    Page<Post> findBy2StacksDesc(Pageable pageable, @Param("stack1")String stack1, @Param("stack2")String stack2);

    @Query("select p from Post p where p.stack like %:stack1% or p.stack like %:stack2% or p.stack like %:stack3% order by p.date desc")
    Page<Post> findBy3StacksDesc(Pageable pageable, @Param("stack1")String stack1, @Param("stack2")String stack2, @Param("stack3")String stack3);

    @Query("select p from Post p where p.date>:date and p.stack like %:stack% order by p.date desc")
    List<Post> find1RecommendNDaysBefore(@Param("date") String date, @Param("stack")String stack);

    @Query("select p from Post p where p.date>:date and p.stack like %:stack1% or p.stack like %:stack2% order by p.date desc")
    List<Post> find2RecommendNDaysBefore(@Param("date") String date, @Param("stack1")String stack1, @Param("stack2")String stack2);

    @Query("select p from Post p where p.date>:date and p.stack like %:stack1% or p.stack like %:stack2% or p.stack like %:stack3% order by p.date desc")
    List<Post> find3RecommendNDaysBefore(@Param("date") String date, @Param("stack1")String stack1, @Param("stack2")String stack2, @Param("stack3")String stack3);
}
