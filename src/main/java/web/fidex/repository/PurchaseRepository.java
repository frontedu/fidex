package web.fidex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.fidex.model.fidex_model.Purchase;
import web.fidex.repository.queries.purchase.PurchaseQueries;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>, PurchaseQueries {
    
}
