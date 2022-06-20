package Matching.SouP.crawler.inflearn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InflearnRepository extends JpaRepository<Inflearn,Long> {
    @Query("select MAX(i.num) from Inflearn i")
    Long findRecent();

    @Query("select i from Inflearn i order by i.id DESC")
    List<Inflearn> findAllDesc();

    List<Inflearn> findTop8ByOrderByIdDesc();

    @Query("select i from Inflearn i where i.num=:num")
    Inflearn findByNum(@Param("num") int num);
}
