package web.fidex.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.Usuario;
import web.fidex.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Cacheable(cacheNames = "usuarios", key = "#nomeUsuario?.toLowerCase()", unless = "#result == null")
	public Usuario findByNomeUsuarioIgnoreCase(String nomeUsuario) {
		return usuarioRepository.findByNomeUsuarioIgnoreCase(nomeUsuario);
	}
}
