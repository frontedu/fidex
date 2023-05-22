package web.controlevacinacao.validation.validator;

import java.lang.reflect.Field;
import java.math.BigInteger;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import web.controlevacinacao.validation.BigIntegerAttributesRelation;
import web.controlevacinacao.validation.util.AttributesRelation;

public class BigIntegerAttributesRelationValidator implements ConstraintValidator<BigIntegerAttributesRelation, Object> {

	private String attribute1;
	private String attribute2;
	private AttributesRelation relation;

	@Override
	public void initialize(final BigIntegerAttributesRelation annotation) {
		attribute1 = annotation.attribute1();
		attribute2 = annotation.attribute2();
		relation = annotation.relation();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext ctx) {
		if (value == null) {
			return true;
		}
		Field field1, field2;
		try {
			field1 = value.getClass().getDeclaredField(attribute1);
			field2 = value.getClass().getDeclaredField(attribute2);
		} catch (final Exception ignore) {
			throw new RuntimeException("It was impossible to get the attributes from it's names");
		}
		if (field1.getType() != BigInteger.class || field2.getType() != BigInteger.class) {
			throw new IllegalArgumentException("Attributes should be of type BigInteger");
		}
		field1.setAccessible(true);
		field2.setAccessible(true);

		BigInteger value1, value2;
		try {
			value1 = (BigInteger) field1.get(value);
			value2 = (BigInteger) field2.get(value);
		} catch (final Exception ignore) {
			throw new RuntimeException("It was impossible to get attributes values");
		}

		String message = "";
		boolean valid = false;
		switch (relation) {
		case EQUAL:
			valid = value1.equals(value2);
			if (!valid) {
				message = "Os valores dos atributos são diferentes";
			}
			break;
		case DIFFERENT:
			valid = !value1.equals(value2);
			if (!valid) {
				message = "Os valores dos atributos são iguais";
			}
			break;
		case GREATERTHAN:
			valid = value1.compareTo(value2) == 1;
			if (!valid) {
				message = "O " + attribute1 + " não é maior que " + attribute2;
			}
			break;
		case LESSTHAN:
			valid = value1.compareTo(value2) == -1;
			if (!valid) {
				message = "O " + attribute1 + " não é menor que " + attribute2;
			}
			break;
		case GREATEROREQUAL:
			valid = value1.compareTo(value2) >= 0;
			if (!valid) {
				message = "O " + attribute1 + " não é maior ou igual a " + attribute2;
			}
			break;
		case LESSOREQUAL:
			valid = value1.compareTo(value2) <= 0;
			if (!valid) {
				message = "O " + attribute1 + " não é menor ou igual a " + attribute2;
			}
			break;
		}

		if (!valid) {
			ctx.disableDefaultConstraintViolation();
			ctx.buildConstraintViolationWithTemplate(message).addPropertyNode(attribute1).addConstraintViolation();
		}
		return valid;

	}

}