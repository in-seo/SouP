package Matching.SouP.crawler.CamPick;

import Matching.SouP.domain.project.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
 public interface CampickRepository extends JpaRepository<Campick,Long> {
    @Query("select MAX(c.num) from Campick c")
    Long findRecent();

    @Query("select c from Campick c order by c.id DESC")
    List<Campick> findAllDesc();
}

