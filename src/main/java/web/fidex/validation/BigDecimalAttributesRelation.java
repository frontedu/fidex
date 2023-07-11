package web.fidex.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import web.fidex.validation.util.AttributesRelation;
import web.fidex.validation.validator.BigDecimalAttributesRelationValidator;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BigDecimalAttributesRelationValidator.class)
@Documented
public @interface BigDecimalAttributesRelation {

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
		BigDecimalAttributesRelation[] value();
	}

}
