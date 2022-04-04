package Matching.SouP.crawler.inflearn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InflearnRepository extends JpaRepository<Inflearn,Long> {
    @Query("select MAX(i.num) from Inflearn i")
    Long findRecent();
}
