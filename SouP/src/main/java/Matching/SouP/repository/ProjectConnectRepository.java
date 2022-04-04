package Matching.SouP.repository;

import Matching.SouP.domain.project.ProjectConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectConnectRepository extends JpaRepository<ProjectConnect,Long> {

    @Query("select p from ProjectInfo p where p.projectName = :projectName")
    ProjectConnect findProjectByName(@Param("projectName") String projectName);

//    @Query("select l.id from Project p join People l on p.people.id = l.id")
//    People findPeopleListInProject(Long projectId);
}
