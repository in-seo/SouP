package Matching.SouP.repository;

import Matching.SouP.domain.posts.Lounge;
import Matching.SouP.domain.posts.LoungeConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoungeConnectRepository extends JpaRepository<LoungeConnect,Long> {
    @Query("select lc from LoungeConnect lc where lc.lounge.id = :LoungeId")
    List<LoungeConnect> findByLoungeId(Long LoungeId);

    @Query("select lc from LoungeConnect lc order by lc.createdDate desc")
    List<LoungeConnect> findAllDesc();
}
