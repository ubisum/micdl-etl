package it.almaviva.mic.etl.validation.ade;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdeEdificialitaTerreniValidator.class)
public @interface AdeEdificialitaTerreni 
{
	String message() default "061 - Se il campo edificialita' vale E, il numero dev'essere nella forma .[0-9]{4}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
