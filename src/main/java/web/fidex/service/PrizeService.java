package web.fidex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.fidex_model.Prize;
import web.fidex.repository.PrizeRepository;

@Service
public class PrizeService {
    
    @Autowired
    private PrizeRepository PrizeRepository;

    @Transactional
    public void salvar(Prize Prize) {
        PrizeRepository.save(Prize);
    }

    @Transactional
    public void remover(Long codigo) {
        PrizeRepository.deleteById(codigo);
    }

}
