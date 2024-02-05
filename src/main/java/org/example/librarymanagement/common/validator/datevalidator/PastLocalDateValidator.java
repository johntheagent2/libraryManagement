package org.example.librarymanagement.common.validator.datevalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PastLocalDateValidator implements ConstraintValidator<PastLocalDate, LocalDate> {

    @Override
    public void initialize(PastLocalDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || value.isBefore(LocalDate.now());
    }
}

