package Matching.SouP.repository;

import Matching.SouP.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(@Param("email")String email);

    @Query("select u.id, u.email, u.role from User u where u.email = :email")
    Optional<User> findByEmailWithIndex(@Param("email")String email);

    @Query("select u from User u left join fetch u.postList where u.email = :email")
    Optional<User> findByEmailFetchPL(@Param("email")String email);

    @Query("select u from User u left join fetch u.projectConnectList where u.email = :email")
    Optional<User> findByEmailFetchPC(@Param("email")String email);

    @Query("select u from User u left join fetch u.loungeConnectList where u.email = :email")
    Optional<User> findByEmailFetchLC(@Param("email")String email);

}
