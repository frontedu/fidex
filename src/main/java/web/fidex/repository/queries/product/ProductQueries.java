package web.fidex.repository.queries.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.fidex.model.fidex_model.Product;
import web.fidex.model.filter.ProductFilter;

public interface ProductQueries {

    Page<Product> buscarComFiltro(ProductFilter filtro, Pageable pageable);

}

