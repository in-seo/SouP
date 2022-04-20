package Matching.SouP.repository;

import Matching.SouP.domain.posts.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
}
