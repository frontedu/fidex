package web.fidex.service;

import java.security.InvalidParameterException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.Usuario;
import web.fidex.repository.UsuarioRepository;

@Service
public class NomeUsuarioUnicoServiceImpl implements NomeUsuarioUnicoService {

	private final UsuarioRepository usuarioRepository;

	public NomeUsuarioUnicoServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isValueUnique(Object value, String fieldName) throws UnsupportedOperationException {
		if (!fieldName.equals("nomeUsuario")) {
			throw new UnsupportedOperationException("A anotacao deveria ser usada no atributo nomeUsuario");
		}

		Usuario novo = (Usuario) value;
		if (novo.getNomeUsuario() == null || novo.getNomeUsuario().isBlank()) {
			return true;
		}

		Usuario comEsseNomeUsuario = usuarioRepository.findByNomeUsuarioIgnoreCase(novo.getNomeUsuario());

		if (comEsseNomeUsuario == null) {
			return true;
		}

		if (novo.getCodigo() == null) {
			return false;
		}

		Usuario antigo = usuarioRepository.findById(novo.getCodigo())
				.orElseThrow(() -> new InvalidParameterException("O codigo do usuario a validar nao existe."));
		return comEsseNomeUsuario.equals(antigo);
	}
}
