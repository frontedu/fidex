package web.fidex.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.Usuario;
import web.fidex.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {

	private final UsuarioRepository usuarioRepository;

	public CadastroUsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Transactional
	@CacheEvict(cacheNames = "usuarios", key = "#usuario.nomeUsuario?.toLowerCase()")
	public void salvar(Usuario usuario) {
		usuarioRepository.save(usuario);
	}

	@CacheEvict(cacheNames = "usuarios", key = "#nomeUsuario?.toLowerCase()")
	public void atualizarCashback(String nomeUsuario, Double cashback) {
		usuarioRepository.updateCashback(nomeUsuario, cashback);
	}
}
