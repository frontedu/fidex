package web.fidex.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import web.fidex.model.fidex_model.Product;
import web.fidex.model.fidex_model.Status;
import web.fidex.repository.queries.product.ProductQueries;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueries {
    List<Product> findByStatus(Status status);
    List<Product> findByPriceLessThanEqual(double price);
    Page<Product> findByCreatedBy(String userId, Pageable pageable);

}
