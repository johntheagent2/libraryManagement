package org.example.librarymanagement.common.validator.phonenumber;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.ResourceBundle;

@Validated
@AllArgsConstructor
public class PhoneNumberValidator implements ConstraintValidator<ValidatePhoneNumber, String> {

    private final ResourceBundle lang;

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, lang.getString("phone.phone-validation-region"));
            return phoneNumberUtil.isValidNumber(parsedNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }
}
