package web.fidex.repository.queries.prize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.fidex.model.fidex_model.Prize;
import web.fidex.model.filter.PrizeFilter;

public interface PrizeQueries {

    Page<Prize> buscarComFiltro(PrizeFilter filtro, Pageable pageable);

}

