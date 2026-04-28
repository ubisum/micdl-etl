package it.almaviva.mic.etl.validation.ade;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdeSoggettoCFSessoValidator.class)
public @interface AdeSoggettoCFSesso 
{
	String message() default "In caso di persona fisica, il campo sesso puo' assumere il valore 1 o 2. "
			               + "In caso di persona giuridica, puo' contenere un CF di 11 caratteri";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
