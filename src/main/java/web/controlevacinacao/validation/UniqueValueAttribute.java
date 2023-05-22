package web.controlevacinacao.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import web.controlevacinacao.validation.service.UniqueValue;
import web.controlevacinacao.validation.validator.UniqueValueAttributeValidator;

//How to use:
//
//A) Create an interface
//public interface EmailUnicoService extends UniqueValue { }
//
//B) Create the implementation
//@Service
//public class EmailUnicoServiceImpl implements EmailUnicoService {
//	@Autowired
//	private ContatoRepository contatoRepository;
//
//	@Override
//	public boolean isValueUnique(Object value, String fieldName) throws UnsupportedOperationException {
//		if (!fieldName.equals("email")) {
//			throw new UnsupportedOperationException("A anotação deveria ser usada no atributo email");
//		}
//
//		Contato novo = (Contato) value;
//		//A validacao se foi preenchido um email nao eh obrigacao dessa verificacao
//		if (novo.getEmail() == null || novo.getEmail().isBlank()) {
//			return true;
//		}
//		
//		//Busca um contato com esse email
//		Contato comEsseEmail = contatoRepository.findByEmailIgnoreCase(novo.getEmail());
//		
//		//Nao existe um contato com esse email, entao tudo bem
//		if (comEsseEmail == null) {
//			return true;
//		} else {  //Existe um contato com esse email
//			//Estao tentando validar um novo contato com um email que ja existe 
//			if (novo.getCodigo() == null) {
//				return false;
//			} else {  //O contato sendo validado ja existe
//				Contato antigo = contatoRepository.findById(novo.getCodigo()).orElseThrow(() -> new InvalidParameterException("O código do contato a validar não existe."));
//				// Se o contato sendo validado for o mesmo o mesmo que ja existia no BD entao tudo bem
//				if (comEsseEmail.equals(antigo)) {
//					return true;
//				}
//				// Senao eh pq estao tentando validar um email que eh de outro contato
//				return false;
//			}
//		}
//	}
//}
//
//C) Use the interface in the entity
//@UniqueValueAttribute(attribute = "email", service = EmailUnicoService.class, message = "Já existe um e-mail igual a este cadastrado")
//public class Contato {
//    private String email;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValueAttributeValidator.class)
@Documented
public @interface UniqueValueAttribute {

    String attribute();

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	Class<? extends UniqueValue> service();
	
    String serviceQualifier() default "";
	
	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		UniqueValueAttribute[] value();
	}
}
