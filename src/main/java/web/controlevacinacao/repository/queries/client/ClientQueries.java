package web.controlevacinacao.repository.queries.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.fidex_model.Client;
import web.controlevacinacao.model.filter.ClientFilter;

public interface ClientQueries {
    
    Page<Client> buscarComFiltro(ClientFilter filtro, Pageable pageable);

}
