package web.controlevacinacao.validation.validator;

import java.lang.reflect.Field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import web.controlevacinacao.validation.DoubleAttributesRelation;
import web.controlevacinacao.validation.util.AttributesRelation;
import web.controlevacinacao.validation.util.ReflectUtil;

public class DoubleAttributesRelationValidator implements ConstraintValidator<DoubleAttributesRelation, Object> {

	private String attribute1;
	private String attribute2;
	private AttributesRelation relation;
	private double epsilon;

	@Override
	public void initialize(final DoubleAttributesRelation annotation) {
		attribute1 = annotation.attribute1();
		attribute2 = annotation.attribute2();
		relation = annotation.relation();
		epsilon = annotation.epsilon();
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
		if (!ReflectUtil.isAssignableTo(field1.getType(), Double.class) || !ReflectUtil.isAssignableTo(field2.getType(), Double.class)) {
			throw new IllegalArgumentException("Attributes should be of type Double/double");
		}
		field1.setAccessible(true);
		field2.setAccessible(true);

		double value1, value2;
		try {
			if (field1.getType() == Double.class) {
				Double aux = (Double) field1.get(value);
				if (aux == null) {
					ctx.disableDefaultConstraintViolation();
					ctx.buildConstraintViolationWithTemplate("O atributo1 não pode ser null").addPropertyNode(attribute1).addConstraintViolation();
					return false;
				}
				value1 = aux;
			} else {
				value1 = (double) field1.get(value);
			}
			if (field2.getType() == Double.class) {
				Double aux = (Double) field2.get(value);
				if (aux == null) {
					ctx.disableDefaultConstraintViolation();
					ctx.buildConstraintViolationWithTemplate("O atributo2 não pode ser null").addPropertyNode(attribute1).addConstraintViolation();
					return false;
				}
				value2 = aux;
			} else {
				value2 = (double) field2.get(value);
			}
		} catch (final Exception ignore) {
			throw new RuntimeException("It was impossible to get the attributes values");
		}

		String message = "";
		boolean valid = false;
		switch (relation) {
		case EQUAL:
			valid = Math.abs(value1 - value2) <= epsilon;
			if (!valid) {
				message = "Os valores dos atributos são diferentes";
			}
			break;
		case DIFFERENT:
			valid = Math.abs(value1 - value2) > epsilon;
			if (!valid) {
				message = "Os valores dos atributos são iguais";
			}
			break;
		case GREATERTHAN:
			valid = value1 > value2;
			if (valid && Math.abs(value1 - value2) <= epsilon) {
				valid = false;
			}
			if (!valid) {
				message = "O " + attribute1 + " não é maior que " + attribute2;
			}
			break;
		case LESSTHAN:
			valid = value1 < value2;
			if (valid && Math.abs(value1 - value2) <= epsilon) {
				valid = false;
			}
			if (!valid) {
				message = "O " + attribute1 + " não é menor que " + attribute2;
			}
			break;
		case GREATEROREQUAL:
			valid = value1 > value2 || Math.abs(value1 - value2) <= epsilon;
			if (!valid) {
				message = "O " + attribute1 + " não é maior ou igual a " + attribute2;
			}
			break;
		case LESSOREQUAL:
			valid = value1 < value2 || Math.abs(value1 - value2) <= epsilon;
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
