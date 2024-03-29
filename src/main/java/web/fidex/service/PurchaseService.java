package web.fidex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.fidex_model.Purchase;
import web.fidex.repository.PurchaseRepository;

@Service
public class PurchaseService {
    
    @Autowired
    private PurchaseRepository PurchaseRepository;

    @Transactional
    public void salvar(Purchase Purchase) {
        PurchaseRepository.save(Purchase);
    }

    @Transactional
    public void remover(Long codigo) {
        PurchaseRepository.deleteById(codigo);
    }

}
