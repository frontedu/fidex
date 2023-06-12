package web.controlevacinacao.repository.queries.purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.fidex_model.Purchase;
import web.controlevacinacao.model.filter.PurchaseFilter;

public interface PurchaseQueries {

    Page<Purchase> buscarComFiltro(PurchaseFilter filtro, Pageable pageable);

}

