package web.fidex.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import web.fidex.validation.UniqueValueAttribute;
import web.fidex.validation.service.UniqueValue;

public class UniqueValueAttributeValidator implements ConstraintValidator<UniqueValueAttribute, Object> {

	private String attribute;
	private String message;

	// Removed Autowired applicationContext to avoid null pointer issues

	private UniqueValue service;

	@Override
	public void initialize(final UniqueValueAttribute annotation) {
		attribute = annotation.attribute();
		message = annotation.message();

		Class<? extends UniqueValue> clazz = annotation.service();
		String serviceQualifier = annotation.serviceQualifier();

		// Use BeanUtil to retrieve the bean statically
		if (!serviceQualifier.equals("")) {
			service = web.fidex.config.BeanUtil.getBean(serviceQualifier, clazz);
		} else {
			service = web.fidex.config.BeanUtil.getBean(clazz);
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
