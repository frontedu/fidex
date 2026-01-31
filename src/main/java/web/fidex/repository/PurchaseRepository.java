package web.fidex.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import web.fidex.model.fidex_model.Purchase;
import web.fidex.model.fidex_model.Status;
import web.fidex.repository.queries.purchase.PurchaseQueries;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>, PurchaseQueries {
    Page<Purchase> findByCreatedBy(String userId, Pageable pageable);
}
