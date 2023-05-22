package web.controlevacinacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Lote;
import web.controlevacinacao.repository.LoteRepository;

@Service
public class LoteService {
    
    @Autowired
    private LoteRepository loteRepository;

    @Transactional
    public void salvar(Lote lote) {
        loteRepository.save(lote);
    }

    @Transactional
    public void remover(Long codigo) {
        loteRepository.deleteById(codigo);
    }

}
