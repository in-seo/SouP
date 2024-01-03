package Matching.SouP.repository;

import Matching.SouP.domain.post.Lounge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoungeRepository extends JpaRepository<Lounge,Long> {

    @Query("select l from Lounge l left join fetch User u where l.user.id = u.id order by l.id DESC")
    List<Lounge> findAllDesc();
}
