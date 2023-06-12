package web.controlevacinacao.repository.queries.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.fidex_model.Product;
import web.controlevacinacao.model.filter.ProductFilter;

public interface ProductQueries {

    Page<Product> buscarComFiltro(ProductFilter filtro, Pageable pageable);

}

