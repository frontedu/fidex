package web.controlevacinacao.validation.validator;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import web.controlevacinacao.validation.ValidUsername;

//pelo menos uma letra
//pelo menos um digito
//pelo menos 1 caractere especial
//tamanho mínimo 6
//tamanho máximo 20
//pelo menos 1 caractere maiúsculo
//pelo menos 1 caractere minúsculo
//não pode ser um nome de usuário proibido
public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {

	private static final Logger logger = LoggerFactory.getLogger(ValidUsernameValidator.class);
	
	private static final List<String> LISTA_CARACTERES_ESPECIAIS = Arrays.asList("!", "@", "#", "$", "%", "&", "*", ".", ",", "~", "^", "/",
		"\\", "+", ":", ";", "=", "'", "`", "[", "]", "(", ")", "{", "}", "<", ">", "-", "_");
	private static final List<String> LISTA_NOMES_USUARIO_PROIBIDOS = Arrays.asList("root", "eu", "admin");

	private String mensagemFinal = "";
	
	@Override
	public boolean isValid(String nomeUsuario, ConstraintValidatorContext constraintValidatorContext) {
		mensagemFinal = "";
		if (nomeUsuario != null) {
			//Vai mostrar todos os erros de uma vez.
			boolean valido = peloMenosUmaLetra(nomeUsuario);
			valido &= peloMenosUmDigito(nomeUsuario);
			valido &= peloMenosUmCaractereEspecial(nomeUsuario);
			valido &= tamanhoMinimo(nomeUsuario);
			valido &= tamanhoMaximo(nomeUsuario);
			valido &= peloMenosUmCaractereMaiusculo(nomeUsuario);
			valido &= peloMenosUmCaractereMinusculo(nomeUsuario);
			valido &= naoEhProibido(nomeUsuario);
			
			//Vai mostrar os erros um a um até que ele seja arrumado, nessa ordem.
//			boolean valido = peloMenosUmaLetra(nomeUsuario) &&
//				   peloMenosUmDigito(nomeUsuario) &&
//				   peloMenosUmCaractereEspecial(nomeUsuario) &&
//				   tamanhoMinimo(nomeUsuario) &&
//				   tamanhoMaximo(nomeUsuario) &&
//				   peloMenosUmCaractereMaiusculo(nomeUsuario) &&
//				   peloMenosUmCaractereMinusculo(nomeUsuario) &&
//				   naoEhProibido(nomeUsuario);

			if (!valido) {
				constraintValidatorContext.buildConstraintViolationWithTemplate(mensagemFinal)
				.addConstraintViolation()
				.disableDefaultConstraintViolation();
			}
			return valido;
		} else {
			return true;
		}
	}

	private boolean peloMenosUmaLetra(String nomeUsuario) {
		for (int pos = 0; pos < nomeUsuario.length(); pos++) {
			if (Character.isAlphabetic(nomeUsuario.charAt(pos))) {
				return true;
			}
		}
		acrescentarNaMensagemFinal("Ao menos uma letra");
		return false;
	}
	
	private boolean peloMenosUmDigito(String nomeUsuario) {
		for (int pos = 0; pos < nomeUsuario.length(); pos++) {
			if (Character.isDigit(nomeUsuario.charAt(pos))) {
				return true;
			}
		}
		acrescentarNaMensagemFinal("Ao menos um dígito");
		return false;
	}
	
	private boolean peloMenosUmCaractereEspecial(String nomeUsuario) {
		logger.debug("Recebeu o nomeUsuario: {}", nomeUsuario);
		for (int pos = 0; pos < nomeUsuario.length(); pos++) {
			if (LISTA_CARACTERES_ESPECIAIS.contains(String.valueOf(nomeUsuario.charAt(pos)))) {
				logger.debug("O nomeUsuario: {} tem um caractere especial na posição: {}", nomeUsuario, pos);
				return true;
			}
		}
		acrescentarNaMensagemFinal("Ao menos um caractere especial");
		return false;
	}
	
	private boolean tamanhoMinimo(String nomeUsuario) {
		if (nomeUsuario.length() > 5) {
			return true;
		} else {
			acrescentarNaMensagemFinal("Tamanho mínimo de 5 caracteres");
			return false;
		}
	}

	private boolean tamanhoMaximo(String nomeUsuario) {
		if (nomeUsuario.length() < 21) {
			return true;
		} else {
			acrescentarNaMensagemFinal("Tamanho máximo de 20 caracteres");
			return false;
		}
	}

	private boolean peloMenosUmCaractereMaiusculo(String nomeUsuario) {
		for (int pos = 0; pos < nomeUsuario.length(); pos++) {
			if (Character.isUpperCase(nomeUsuario.charAt(pos))) {
				return true;
			}
		}
		acrescentarNaMensagemFinal("Ao menos um caractere maiúsculo");
		return false;
	}

	private boolean peloMenosUmCaractereMinusculo(String nomeUsuario) {
		for (int pos = 0; pos < nomeUsuario.length(); pos++) {
			if (Character.isLowerCase(nomeUsuario.charAt(pos))) {
				return true;
			}
		}
		acrescentarNaMensagemFinal("Ao menos um caractere minúsculo");
		return false;
	}

	private boolean naoEhProibido(String nomeUsuario) {
		if (!LISTA_NOMES_USUARIO_PROIBIDOS.contains(nomeUsuario.toLowerCase())) {
			return true;
		} else {
			acrescentarNaMensagemFinal("Não pode ser um nome proibido");
			return false;
		}
	}
	
	private void acrescentarNaMensagemFinal(String mensagem) {
		if (!mensagemFinal.isBlank()) {
			mensagemFinal += "; ";
		}
		mensagemFinal += mensagem;
	}
}