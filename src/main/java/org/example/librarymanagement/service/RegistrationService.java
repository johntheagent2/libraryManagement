package org.example.librarymanagement.service;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.UserDTO;
import org.example.librarymanagement.model.account.appUser.AppUser;
import org.example.librarymanagement.model.account.Role;
import org.example.librarymanagement.common.emailSender.EmailSenderService;
import org.example.librarymanagement.dto.RegistrationRequestDTO;
import org.example.librarymanagement.model.token.ConfirmationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;

    public UserDTO register(RegistrationRequestDTO request) throws MessagingException, TemplateException, IOException {
        return appUserService.signupUser(new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getAddress(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getPassword(),
                Role.USER,
                LocalDateTime.now()));
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        verifyExpiryDate(confirmationToken);

        confirmationTokenService.remove(confirmationToken);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());

        return "confirmed";
    }

    @Transactional
    public String confirmOtpToken(String otp){
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationOTP(otp)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        verifyExpiryDate(confirmationToken);

        confirmationTokenService.remove(confirmationToken);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }

    public void verifyExpiryDate(ConfirmationToken confirmationToken){
        LocalDateTime expiredDateTime = confirmationToken.getExpiresAt();

        if(confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token has already confirmed!");
        }

        if (expiredDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token is expired!");
        }
    }

    public String resendToken(String email) throws MessagingException, TemplateException, IOException {
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationTokenByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        LocalDateTime newExpires =  LocalDateTime.now().plusMinutes(15);
        confirmationTokenService.setNewExpired(confirmationToken.getToken(), newExpires);
//        emailSenderService.sendConfirmationMail(confirmationToken.getToken(),
//                confirmationToken.getAppUser().getEmail());
        return "updated";
    }
}
