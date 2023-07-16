package web.fidex.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import web.fidex.model.fidex_model.Prize;
import web.fidex.model.fidex_model.Status;
import web.fidex.repository.queries.prize.PrizeQueries;

public interface PrizeRepository extends JpaRepository<Prize, Long>, PrizeQueries {
      List<Prize> findByStatus(Status status);
      Page<Prize> findByCreatedBy(String userId, Pageable pageable);

}
