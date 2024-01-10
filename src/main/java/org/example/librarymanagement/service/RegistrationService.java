package org.example.librarymanagement.service;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.UserDTO;
import org.example.librarymanagement.exception.serviceException.ServiceException;
import org.example.librarymanagement.model.account.appUser.AppUser;
import org.example.librarymanagement.model.account.Role;
import org.example.librarymanagement.common.emailSender.EmailSenderService;
import org.example.librarymanagement.dto.RegistrationRequestDTO;
import org.example.librarymanagement.model.token.ConfirmationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final ResourceBundle resourceBundle;

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
                .orElseThrow(() -> new ServiceException("confirmation-token.link.link-not-found",
                        resourceBundle.getString("confirmation-token.link.link-not-found")));

        verifyExpiryDate(confirmationToken);

        confirmationTokenService.remove(confirmationToken);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());

        return "confirmed";
    }

    @Transactional
    public String confirmOtpToken(String otp){
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationOTP(otp)
                .orElseThrow(() -> new ServiceException("confirmation-token.otp.otp-expired",
                        resourceBundle.getString("confirmation-token.otp.otp-expired")));

        verifyExpiryDate(confirmationToken);

        confirmationTokenService.remove(confirmationToken);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }

    public void verifyExpiryDate(ConfirmationToken confirmationToken){
        LocalDateTime expiredDateTime = confirmationToken.getExpiresAt();

        if(confirmationToken.getConfirmedAt() != null) {
            throw new ServiceException("confirmation-token.link.link-already-confirmed",
                    resourceBundle.getString("confirmation-token.link.link-already-confirmed"));
        }

        if (expiredDateTime.isBefore(LocalDateTime.now())) {
            throw new ServiceException("confirmation-token.link.link-expired",
                    resourceBundle.getString("confirmation-token.link.link-expired"));
        }
    }

    public String resendToken(String email) {
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationTokenByEmail(email)
                .orElseThrow(() -> new ServiceException("confirmation-token.link.link-not-found",
                        resourceBundle.getString("confirmation-token.link.link-not-found")));

        appUserService.resendVerification(confirmationToken);
        return "updated";
    }
}
