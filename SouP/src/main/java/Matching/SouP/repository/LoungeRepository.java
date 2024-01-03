package Matching.SouP.repository;

import Matching.SouP.domain.post.Lounge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoungeRepository extends JpaRepository<Lounge,Long> {

    @Query(value = "select l from Lounge l left join fetch l.user")
    List<Lounge> findAllDesc();
}
