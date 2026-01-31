package web.fidex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.fidex_model.Purchase;
import web.fidex.repository.PurchaseRepository;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Transactional
    public void salvar(Purchase purchase) {
        purchaseRepository.save(purchase);
    }

    @Transactional
    public void remover(Long codigo) {
        purchaseRepository.deleteById(codigo);
    }

}
