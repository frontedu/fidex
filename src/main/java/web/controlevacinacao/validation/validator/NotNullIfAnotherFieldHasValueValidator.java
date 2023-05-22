package web.controlevacinacao.validation.validator;

import org.apache.commons.beanutils.BeanUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import web.controlevacinacao.validation.NotNullIfAnotherFieldHasValue;

public class NotNullIfAnotherFieldHasValueValidator implements ConstraintValidator<NotNullIfAnotherFieldHasValue, Object> {

	private String fieldName;
	private String expectedFieldValue;
	private String dependFieldName;

	@Override
	public void initialize(final NotNullIfAnotherFieldHasValue annotation) {
		fieldName = annotation.fieldName();
		expectedFieldValue = annotation.fieldValue();
		dependFieldName = annotation.dependFieldName();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext ctx) {
		if (value == null) {
			return true;
		}
		try {
			final String fieldValue = BeanUtils.getProperty(value, fieldName);
			final String dependFieldValue = BeanUtils.getProperty(value, dependFieldName);
			if (expectedFieldValue.equals(fieldValue) && dependFieldValue == null) {
				ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
				    .addPropertyNode(dependFieldName)
				    .addConstraintViolation()
				    .disableDefaultConstraintViolation();
				return false;
			}
		} catch (final Exception ignore) {
			// we can ignore
		}
		return true;
	}
}