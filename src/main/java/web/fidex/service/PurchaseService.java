package web.fidex.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.fidex_model.Purchase;
import web.fidex.repository.PurchaseRepository;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional
    public void salvar(Purchase purchase) {
        purchaseRepository.save(purchase);
    }

    @Transactional
    public void remover(Long codigo) {
        purchaseRepository.deleteById(codigo);
    }

}
