package web.fidex.validation.validator;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import web.fidex.validation.UniqueValueAttribute;
import web.fidex.validation.service.UniqueValue;

@Component
public class UniqueValueAttributeValidator implements ConstraintValidator<UniqueValueAttribute, Object> {

	private final ApplicationContext applicationContext;
	private String attribute;
	private String message;
	private UniqueValue service;

	public UniqueValueAttributeValidator(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void initialize(final UniqueValueAttribute annotation) {
		attribute = annotation.attribute();
		message = annotation.message();

		Class<? extends UniqueValue> clazz = annotation.service();
		String serviceQualifier = annotation.serviceQualifier();

		if (serviceQualifier != null && !serviceQualifier.isBlank()) {
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
