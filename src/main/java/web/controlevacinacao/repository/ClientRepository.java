package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.fidex_model.Client;
import web.controlevacinacao.repository.queries.client.ClientQueries;

public interface ClientRepository extends JpaRepository<Client, Long>, ClientQueries {
    
}
