package Matching.SouP.repository;

import Matching.SouP.domain.project.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfo,Long> {

    @Query("select p from ProjectInfo p order by p.id DESC")
    List<ProjectInfo> findAllDesc();
}