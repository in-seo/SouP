package Matching.SouP.repository;

import Matching.SouP.domain.project.ProjectConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectConnectRepository extends JpaRepository<ProjectConnect,Long> {


    @Query("select c from ProjectConnect c where c.projectInfo.id=:id")
    ProjectConnect findProjectConnectByProjectInfo(Long id);
}
