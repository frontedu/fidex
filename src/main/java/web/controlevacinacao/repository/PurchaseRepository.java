package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.fidex_model.Purchase;
import web.controlevacinacao.repository.queries.purchase.PurchaseQueries;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>, PurchaseQueries {
    
}
