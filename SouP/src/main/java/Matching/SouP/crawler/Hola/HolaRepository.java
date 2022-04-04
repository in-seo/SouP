package Matching.SouP.crawler.Hola;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HolaRepository extends JpaRepository<Hola,Long> {
    @Query("select MAX(h.id) from Hola h")
    Long findRecent();
}
