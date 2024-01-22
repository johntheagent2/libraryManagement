package org.example.librarymanagement.common.validator.changepassword.changed;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.config.security.jwtConfig.JwtService;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ResourceBundle;

@Validated
@AllArgsConstructor
public class ChangedPasswordValidator implements ConstraintValidator<ValidateChangedPassword, String> {

    private final JwtService jwtService;
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(String newPassword, ConstraintValidatorContext context) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwtTokenBearer = request.getHeader("Authorization");
        String jwtToken = jwtService.extractJwtToken(jwtTokenBearer);
        AppUser appUser = appUserService.getAppUser(jwtService.extractEmail(jwtToken));

        return !passwordEncoder.matches(newPassword, appUser.getPassword());
    }
}
