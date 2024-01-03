package Matching.SouP.repository;

import Matching.SouP.domain.post.Lounge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoungeRepository extends JpaRepository<Lounge,Long> {

    @Query(value = "select * from lounge l left join user u on u.user_id = l.user_id", nativeQuery = true)
    List<Lounge> findAllDesc();
}
