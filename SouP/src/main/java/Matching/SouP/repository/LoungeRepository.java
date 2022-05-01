package Matching.SouP.repository;

import Matching.SouP.crawler.okky.Okky;
import Matching.SouP.domain.posts.Lounge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoungeRepository extends JpaRepository<Lounge,Long> {

    @Query("select l from Lounge l order by l.createdDate DESC")
    List<Lounge> findAllDesc();
}
