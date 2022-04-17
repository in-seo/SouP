package Matching.SouP.crawler.Hola;

import Matching.SouP.crawler.CamPick.Campick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolaRepository extends JpaRepository<Hola,Long> {
    @Query("select MAX(h.id) from Hola h")
    Long findRecent();

    @Query("select h from Hola h order by h.id DESC")
    List<Hola> findAllDesc();
}
