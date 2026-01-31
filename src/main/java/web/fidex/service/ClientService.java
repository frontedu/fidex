package web.fidex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.cache.annotation.Cacheable;

import web.fidex.model.fidex_model.Client;
import web.fidex.repository.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public void salvar(Client client) {
        clientRepository.save(client);
    }

    @Transactional
    public void remover(Long codigo) {
        clientRepository.deleteById(codigo);
    }

    @Cacheable("clients")
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

}
