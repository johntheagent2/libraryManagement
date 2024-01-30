package org.example.librarymanagement.common.validator.datevalidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastLocalDateValidator.class)
public @interface PastLocalDate {
    String message() default "Date must be in the past";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
