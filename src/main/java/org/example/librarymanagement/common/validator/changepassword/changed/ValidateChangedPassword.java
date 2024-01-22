package org.example.librarymanagement.common.validator.changepassword.changed;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.librarymanagement.common.validator.phonenumber.PhoneNumberValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ChangedPasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateChangedPassword {

    String message() default "Changed password is the same with current password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
