package Matching.SouP.repository;

import Matching.SouP.config.auth.dto.CoveringUser;
import Matching.SouP.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(@Param("email")String email);

    @Query(value = "select user_id, email, role from user where email = :email", nativeQuery = true)
    Optional<CoveringUser> findByEmailWithIndex(@Param("email")String email);

    @Query("select u from User u left join fetch u.postList where u.email = :email")
    Optional<User> findByEmailFetchPL(@Param("email")String email);

    @Query("select u from User u left join fetch u.projectConnectList where u.email = :email")
    Optional<User> findByEmailFetchPC(@Param("email")String email);

    @Query("select u from User u left join fetch u.loungeConnectList where u.email = :email")
    Optional<User> findByEmailFetchLC(@Param("email")String email);

}
