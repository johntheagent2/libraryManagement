package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.common.email.EmailSenderService;
import org.example.librarymanagement.dto.request.RegistrationRequest;
import org.example.librarymanagement.entity.ConfirmationToken;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.enumeration.Role;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.service.AppUserService;
import org.example.librarymanagement.service.ConfirmationTokenService;
import org.example.librarymanagement.service.RegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

@Service
@AllArgsConstructor
public class RegistrationServiceImp implements RegistrationService{

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final ResourceBundle resourceBundle;
    private final EmailSenderService emailSenderService;


    @Override
    public void register(RegistrationRequest request){
        if(appUserService.findByEmail(request.getEmail()).isPresent()){
            throw new BadRequestException("user.email.email-existed",
                    resourceBundle.getString("user.email.email-existed"));
        }

        AppUser appUser = new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getAddress(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getPassword(),
                Role.ROLE_USER,
                AccountStatus.UNVERIFIED);

        appUserService.saveUser(appUser);

        ConfirmationToken confirmationToken = confirmationTokenService.createToken(appUser);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

//        emailSenderService.sendConfirmationMail(
//                confirmationToken.getToken(),
//                confirmationToken.getOtp(),
//                request.getEmail());

    }

    @Override
    @Transactional
    public void confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationToken(token)
                .orElseThrow(() -> new NotFoundException("confirmation-token.link.link-not-found",
                        resourceBundle.getString("confirmation-token.link.link-not-found")));

        verifyExpiryDate(confirmationToken);

        confirmationTokenService.remove(confirmationToken);
        appUserService.verifyUser(confirmationToken.getAppUser());
    }

    @Override
    @Transactional
    public void confirmOtpToken(String otp){
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationOTP(otp)
                .orElseThrow(() -> new BadRequestException("confirmation-token.otp.otp-expired",
                        resourceBundle.getString("confirmation-token.otp.otp-expired")));

        verifyExpiryDate(confirmationToken);

        confirmationTokenService.remove(confirmationToken);
        appUserService.verifyUser(confirmationToken.getAppUser());
    }

    @Override
    public void verifyExpiryDate(ConfirmationToken confirmationToken){
        LocalDateTime expiredDateTime = confirmationToken.getExpiresAt();

        if (expiredDateTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("confirmation-token.link.link-expired",
                    resourceBundle.getString("confirmation-token.link.link-expired"));
        }
    }

    @Override
    public void resendToken(String email) {
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationTokenByEmail(email)
                .orElseThrow(() -> new NotFoundException("confirmation-token.link.link-not-found",
                        resourceBundle.getString("confirmation-token.link.link-not-found")));

        confirmationToken = confirmationTokenService.refreshToken(confirmationToken);

//        emailSenderService.sendConfirmationMail(
//                confirmationToken.getToken(),
//                confirmationToken.getOtp(),
//                confirmationToken.getAppUser().getEmail()
//        );
    }
}
