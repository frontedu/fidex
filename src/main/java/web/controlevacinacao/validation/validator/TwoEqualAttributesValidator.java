package web.controlevacinacao.validation.validator;

import java.lang.reflect.Field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import web.controlevacinacao.validation.TwoEqualAttributes;

//@TwoEqualAttributes(attribute1 = "email", attribute2 = "emailRepetido")
//
//@TwoEqualAttributes.List({ 
//	@TwoEqualAttributes(attribute1 = "atr1", attribute2 = "atr2"),
//	@TwoEqualAttributes(attribute1 = "atr3", attribute2 = "atr4")
//})
public class TwoEqualAttributesValidator implements ConstraintValidator<TwoEqualAttributes, Object> {

	private String attribute1;
	private String attribute2;
	private String message;

	@Override
	public void initialize(final TwoEqualAttributes constraintAnnotation) {
		attribute1 = constraintAnnotation.attribute1();  // "email"
		attribute2 = constraintAnnotation.attribute2();  // "emailAgain" 
		message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		boolean valid = true;
		try {
			Field field1 = value.getClass().getDeclaredField(attribute1);
			field1.setAccessible(true);
			final Object value1 = field1.get(value);
			Field field2 = value.getClass().getDeclaredField(attribute2);
			field2.setAccessible(true);
			final Object value2 = field2.get(value);
			
//			import org.springframework.beans.BeanWrapperImpl;
//			final Object firstObj = new BeanWrapperImpl(value).getPropertyValue(attribute1);
//			final Object secondObj = new BeanWrapperImpl(value).getPropertyValue(attribute2);

			valid = value1 == null && value2 == null || value1 != null && value1.equals(value2);
		} catch (final Exception ignore) {
			// we can ignore
		}

		if (!valid){
			context.buildConstraintViolationWithTemplate(message)
				.addPropertyNode(attribute1)
				.addConstraintViolation()
				.disableDefaultConstraintViolation();
		}

		return valid;
	}
}