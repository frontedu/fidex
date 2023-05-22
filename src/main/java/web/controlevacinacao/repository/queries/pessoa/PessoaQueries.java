package web.controlevacinacao.repository.queries.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.filter.PessoaFilter;

public interface PessoaQueries {
    
    Page<Pessoa> filtrar(PessoaFilter filtro, Pageable pageable);

}
