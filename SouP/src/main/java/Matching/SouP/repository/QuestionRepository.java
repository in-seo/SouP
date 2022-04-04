package Matching.SouP.repository;

import Matching.SouP.domain.project.Project_Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Project_Question,Long> {
}
