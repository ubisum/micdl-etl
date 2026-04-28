package it.almaviva.mic.etl.validation.ade;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = DataValidaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataValida {

    String message() default "Una o piu' date nel record non sono valide";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

