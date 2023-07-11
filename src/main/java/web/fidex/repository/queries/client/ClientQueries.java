package web.fidex.repository.queries.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.fidex.model.fidex_model.Client;
import web.fidex.model.filter.ClientFilter;

public interface ClientQueries {
    
    Page<Client> buscarComFiltro(ClientFilter filtro, Pageable pageable);

}
