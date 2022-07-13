package Matching.SouP.repository;

import Matching.SouP.domain.post.LoungeConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoungeConnectRepository extends JpaRepository<LoungeConnect,Long> {
    @Query("select lc from LoungeConnect lc where lc.lounge.id = :LoungeId")
    List<LoungeConnect> findByLoungeId(@Param("LoungeId") Long LoungeId);

    @Query("select lc from LoungeConnect lc order by lc.createdDate desc")
    List<LoungeConnect> findAllDesc();
}
