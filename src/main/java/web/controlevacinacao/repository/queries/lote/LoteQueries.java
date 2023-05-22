package web.controlevacinacao.repository.queries.lote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.filter.LoteFilter;

public interface LoteQueries {
    
    Page<Lote> buscarComFiltro(LoteFilter filtro, Pageable pageable);

    Lote buscarComVacina(Long codigo);

}
