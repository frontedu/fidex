package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.fidex_model.Prize;
import web.controlevacinacao.repository.queries.prize.PrizeQueries;

public interface PrizeRepository extends JpaRepository<Prize, Long>, PrizeQueries {
    
}
