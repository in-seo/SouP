package Matching.SouP.crawler.okky;

import Matching.SouP.crawler.inflearn.Inflearn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OkkyRepository extends JpaRepository<Okky,Long> {
    @Query("select MAX(o.num) from Okky o")
    Long findRecent();

    @Query("select o from Okky o order by o.id DESC")
    List<Okky> findAllDesc();

    @Query("select o from Okky o where o.num=:num")
    Okky findByNum(int num);
}
