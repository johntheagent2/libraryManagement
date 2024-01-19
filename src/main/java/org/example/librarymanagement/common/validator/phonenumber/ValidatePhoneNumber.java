package org.example.librarymanagement.common.validator.phonenumber;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePhoneNumber {

    String message() default "Invalid Vietnam phone number format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
