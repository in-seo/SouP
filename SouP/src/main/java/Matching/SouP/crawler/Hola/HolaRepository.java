package Matching.SouP.crawler.Hola;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolaRepository extends JpaRepository<Hola,Long> {
    @Query("select COUNT(h) from Hola h")
    Long getPostCount();

    @Query("select h from Hola h order by h.id DESC")
    List<Hola> findAllDesc();

    @Query("select MAX(h.num) from Hola h")
    String findRecent();
}
