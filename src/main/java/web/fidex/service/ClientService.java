package web.fidex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.fidex_model.Client;
import web.fidex.repository.ClientRepository;

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

    public List<Client> getAllClients() {
        return ClientRepository.findAll();
    }

}
