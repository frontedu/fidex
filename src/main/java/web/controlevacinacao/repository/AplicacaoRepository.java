package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Aplicacao;

public interface AplicacaoRepository extends JpaRepository<Aplicacao, Long> {
    
}
