package org.example.librarymanagement.registration;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.account.appUser.AppUser;
import org.example.librarymanagement.account.appUser.AppUserService;
import org.example.librarymanagement.account.Role;
import org.example.librarymanagement.registration.token.ConfirmationToken;
import org.example.librarymanagement.registration.token.ConfirmationTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request){
        boolean isEmailValid = emailValidator.test(request.getEmail());

        if(!isEmailValid){
            throw new IllegalStateException("Email is not valid");
        }
        return appUserService.signupUser(new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getAddress(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getPassword(),
                LocalDateTime.now()));
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
        LocalDateTime expiredDateTime = confirmationToken.getExpiresAt();
        int isConfirmed;

        if(confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token has already confirmed!");
        }

        if (expiredDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token is expired!");
        }

        isConfirmed = confirmationTokenService.setConfirmationTokenAt(token);

        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        if(isConfirmed == 1){
            return "confirmed";
        }else{
            return "is not";
        }
    }
}
