package org.example.librarymanagement.common.validator.status;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.springframework.validation.annotation.Validated;

@Validated
@AllArgsConstructor
public class AccountStatusValidator implements ConstraintValidator<ValidateAccountStatus, String> {

    @Override
    public boolean isValid(String status, ConstraintValidatorContext context) {
        if (status.equals(AccountStatus.ACTIVE.name())
                || status.equals(AccountStatus.UNVERIFIED.name())
                || status.equals(AccountStatus.BLOCKED.name())){
            return true;
        }

        return false;
    }
}
