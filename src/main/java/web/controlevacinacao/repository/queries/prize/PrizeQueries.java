package web.controlevacinacao.repository.queries.prize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.fidex_model.Prize;
import web.controlevacinacao.model.filter.PrizeFilter;

public interface PrizeQueries {

    Page<Prize> buscarComFiltro(PrizeFilter filtro, Pageable pageable);

}

