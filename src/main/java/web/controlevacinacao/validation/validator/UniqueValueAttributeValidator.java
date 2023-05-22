package web.controlevacinacao.validation.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import web.controlevacinacao.validation.UniqueValueAttribute;
import web.controlevacinacao.validation.service.UniqueValue;

public class UniqueValueAttributeValidator implements ConstraintValidator<UniqueValueAttribute, Object> {

	private String attribute;
	private String message;

	@Autowired
	private ApplicationContext applicationContext;

	private UniqueValue service;

	@Override
	public void initialize(final UniqueValueAttribute annotation) {
		attribute = annotation.attribute();
		message = annotation.message();

		Class<? extends UniqueValue> clazz = annotation.service();
		String serviceQualifier = annotation.serviceQualifier();
		if (!serviceQualifier.equals("")) {
			service = applicationContext.getBean(serviceQualifier, clazz);
		} else {
			service = applicationContext.getBean(clazz);
		}
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext ctx) {
		if (value == null) {
			return true;
		}
		boolean valid = service.isValueUnique(value, attribute);

		if (!valid) {
			ctx.disableDefaultConstraintViolation();
			ctx.buildConstraintViolationWithTemplate(message).addPropertyNode(attribute).addConstraintViolation();
		}
		return valid;
	}

}
