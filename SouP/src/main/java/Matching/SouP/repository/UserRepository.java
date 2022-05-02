package Matching.SouP.repository;

import Matching.SouP.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query("select u from User u left join fetch u.postList where u.email = :email")
    Optional<User> findByFetchEmail(String email);

    @Query("select u from User u where u.nickName = :nick")
    Optional<User> findByNick(String nick);
}
