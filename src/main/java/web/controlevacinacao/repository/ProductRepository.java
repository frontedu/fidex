package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.fidex_model.Product;
import web.controlevacinacao.repository.queries.product.ProductQueries;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueries {
    
}
