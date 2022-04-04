package Matching.SouP.crawler.CamPick;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

 @Repository
 public interface CampickRepository extends JpaRepository<Campick,Long> {
    @Query("select MAX(c.num) from Campick c")
    Long findRecent();
}

