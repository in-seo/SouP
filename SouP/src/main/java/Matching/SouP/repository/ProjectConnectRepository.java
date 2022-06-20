package Matching.SouP.repository;

import Matching.SouP.domain.project.ProjectConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectConnectRepository extends JpaRepository<ProjectConnect,Long> {


    @Query("select pc from ProjectConnect pc left join fetch pc.user where pc.post.id=:id")
    List<ProjectConnect> findByPostId(@Param("id") Long id);

    @Query("select pc from ProjectConnect pc order by pc.createdDate desc")
    List<ProjectConnect> findAllDesc();
}
