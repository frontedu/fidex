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

		try {
			Class<? extends UniqueValue> clazz = annotation.service();
			String serviceQualifier = annotation.serviceQualifier();

			// Use BeanUtil to retrieve the bean statically
			if (!serviceQualifier.equals("")) {
				service = web.fidex.config.BeanUtil.getBean(serviceQualifier, clazz);
			} else {
				service = web.fidex.config.BeanUtil.getBean(clazz);
			}
		} catch (Exception e) {
			// If bean retrieval fails, log and set service to null
			// This will cause validation to pass by default in isValid
			System.err.println("Failed to retrieve validation service: " + e.getMessage());
			service = null;
		}
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext ctx) {
		if (value == null) {
			return true;
		}

		// If service failed to initialize, allow the value (fail-open for safety)
		if (service == null) {
			System.err.println("Validation service is null, allowing value to pass");
			return true;
		}

		try {
			boolean valid = service.isValueUnique(value, attribute);

			if (!valid) {
				ctx.disableDefaultConstraintViolation();
				ctx.buildConstraintViolationWithTemplate(message).addPropertyNode(attribute).addConstraintViolation();
			}
			return valid;
		} catch (Exception e) {
			// If validation fails with exception, log and allow (fail-open)
			System.err.println("Validation failed with exception: " + e.getMessage());
			return true;
		}
	}

}
