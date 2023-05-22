package web.controlevacinacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.repository.VacinaRepository;

@Service
public class VacinaService {
    
    @Autowired
    private VacinaRepository vacinaRepository;

    @Transactional
    public void salvar(Vacina vacina) {
        vacinaRepository.save(vacina);
    }

    @Transactional
    public void remover(Long codigo) {
        vacinaRepository.deleteById(codigo);
    }

}
