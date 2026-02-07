package web.fidex.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.fidex_model.Prize;
import web.fidex.repository.PrizeRepository;

@Service
public class PrizeService {

    private final PrizeRepository prizeRepository;

    public PrizeService(PrizeRepository prizeRepository) {
        this.prizeRepository = prizeRepository;
    }

    @Transactional
    public void salvar(Prize prize) {
        prizeRepository.save(prize);
    }

    @Transactional
    public void remover(Long codigo) {
        prizeRepository.deleteById(codigo);
    }

}
