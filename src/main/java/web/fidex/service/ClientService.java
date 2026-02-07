package web.fidex.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Status;
import web.fidex.repository.ClientRepository;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void salvar(Client client) {
        clientRepository.save(client);
    }

    @Transactional
    public void remover(Long codigo) {
        clientRepository.deleteById(codigo);
    }

    @Transactional(readOnly = true)
    public List<Client> getAllActiveByUser(String createdBy) {
        return clientRepository.findByCreatedByAndStatus(createdBy, Status.ATIVO);
    }
}
