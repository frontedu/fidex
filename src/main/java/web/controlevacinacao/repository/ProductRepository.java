package web.controlevacinacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import web.controlevacinacao.model.fidex_model.Product;
import web.controlevacinacao.model.fidex_model.Status;
import web.controlevacinacao.repository.queries.product.ProductQueries;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueries {
    List<Product> findByStatus(Status status);
    List<Product> findByPriceLessThanEqual(double price);
}
