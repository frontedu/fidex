package web.controlevacinacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.fidex_model.Client;
import web.controlevacinacao.repository.ClientRepository;

@Service
public class ClientService {
    
    @Autowired
    private ClientRepository ClientRepository;

    @Transactional
    public void salvar(Client Client) {
        ClientRepository.save(Client);
    }

    @Transactional
    public void remover(Long codigo) {
        ClientRepository.deleteById(codigo);
    }

}
