package web.fidex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.fidex.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Usuario findByNomeUsuarioIgnoreCase(String nomeUsuario);
	
}
