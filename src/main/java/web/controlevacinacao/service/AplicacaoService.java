package web.controlevacinacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Aplicacao;
import web.controlevacinacao.model.Lote;
import web.controlevacinacao.repository.AplicacaoRepository;
import web.controlevacinacao.repository.LoteRepository;

@Service
public class AplicacaoService {
    
    @Autowired
    private AplicacaoRepository aplicacaoRepository;

    @Autowired
    private LoteRepository loteRepository;

    @Transactional
    public void salvar(Aplicacao aplicacao) {
        Lote lote = aplicacao.getLote();
        lote.setNroDosesAtual(lote.getNroDosesAtual() - 1);
        aplicacaoRepository.save(aplicacao);
        loteRepository.save(lote);
    }

}
