package web.fidex.repository.queries.purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.fidex.model.fidex_model.Purchase;
import web.fidex.model.filter.PurchaseFilter;

public interface PurchaseQueries {

    Page<Purchase> buscarComFiltro(PurchaseFilter filtro, Pageable pageable);

}

