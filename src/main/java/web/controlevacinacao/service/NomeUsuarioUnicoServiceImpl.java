package web.controlevacinacao.service;

import java.security.InvalidParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.controlevacinacao.model.Usuario;
import web.controlevacinacao.repository.UsuarioRepository;

@Service
public class NomeUsuarioUnicoServiceImpl implements NomeUsuarioUnicoService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public boolean isValueUnique(Object value, String fieldName) throws UnsupportedOperationException {
		if (!fieldName.equals("nomeUsuario")) {
			throw new UnsupportedOperationException("A anotação deveria ser usada no atributo nomeUsuario");
		}

		Usuario novo = (Usuario) value;
		//A validacao se foi preenchido um nomeUsuario nao eh obrigacao dessa verificacao
		if (novo.getNomeUsuario() == null || novo.getNomeUsuario().isBlank()) {
			return true;
		}
		
		//Busca um usuario com esse nomeUsuario
		Usuario comEsseNomeUsuario = usuarioRepository.findByNomeUsuarioIgnoreCase(novo.getNomeUsuario());
		
		//Nao existe um usuario com esse nomeUsuario, entao tudo bem
		if (comEsseNomeUsuario == null) {
			return true;
		} else {  //Existe um contato com esse nomeUsuario
			//Estao tentando validar um novo usuario com um nomeUsuario que ja existe 
			if (novo.getCodigo() == null) {
				return false;
			} else {  //O usuario sendo validado ja existe
				Usuario antigo = usuarioRepository.findById(novo.getCodigo()).orElseThrow(() -> new InvalidParameterException("O código do usuario a validar não existe."));
				// Se o usuario sendo validado for o mesmo que ja existia no BD entao tudo bem
				if (comEsseNomeUsuario.equals(antigo)) {
					return true;
				}
				// Senao eh pq estao tentando validar um nomeUsuario que eh de outro usuario
				return false;
			}
		}
	}
}
