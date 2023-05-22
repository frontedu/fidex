package web.controlevacinacao.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import web.controlevacinacao.validation.validator.WellFormedEmailValidator;

@Constraint(validatedBy = WellFormedEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Documented
public @interface WellFormedEmail {

	public String message() default "O formato do e-mail não é válido";

	public Class<?>[] groups() default {};

	public Class<? extends Payload>[] payload() default{};

}