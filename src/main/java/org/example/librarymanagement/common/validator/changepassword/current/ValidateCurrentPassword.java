package org.example.librarymanagement.common.validator.changepassword.current;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.librarymanagement.common.validator.changepassword.changed.ChangedPasswordValidator;
import org.example.librarymanagement.common.validator.phonenumber.PhoneNumberValidator;
import org.hibernate.dialect.function.CurrentFunction;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrentPasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateCurrentPassword {

    String message() default "Current password is not correct";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
