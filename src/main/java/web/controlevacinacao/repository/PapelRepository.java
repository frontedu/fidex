package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Papel;

public interface PapelRepository extends JpaRepository<Papel, Long> {
}
