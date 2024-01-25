package org.example.librarymanagement.common.validator.status;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.librarymanagement.common.validator.phonenumber.PhoneNumberValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AccountStatusValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateAccountStatus {

    String message() default "Invalid account status";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
