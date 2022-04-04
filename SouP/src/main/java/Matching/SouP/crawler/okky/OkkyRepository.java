package Matching.SouP.crawler.okky;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OkkyRepository extends JpaRepository<Okky,Long> {
    @Query("select MAX(o.id) from Okky o")
    Long findRecent();
}
