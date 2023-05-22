package web.controlevacinacao.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import web.controlevacinacao.validation.util.AttributesRelation;
import web.controlevacinacao.validation.validator.IntegerAttributesRelationValidator;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IntegerAttributesRelationValidator.class)
@Documented
public @interface IntegerAttributesRelation {

	String attribute1();

	String attribute2();

	AttributesRelation relation();

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		IntegerAttributesRelation[] value();
	}

}
