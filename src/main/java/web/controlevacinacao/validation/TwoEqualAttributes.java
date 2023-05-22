package web.controlevacinacao.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import web.controlevacinacao.validation.validator.TwoEqualAttributesValidator;

@Constraint(validatedBy = TwoEqualAttributesValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TwoEqualAttributes {
	String message() default "Os valores são diferentes";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
	String attribute1();
	String attribute2();

	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List
	{
		TwoEqualAttributes[] value();
	}
}