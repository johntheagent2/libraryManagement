package org.example.librarymanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.ChangePasswordRequest;
import org.example.librarymanagement.dto.request.ResetPasswordRequest;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.exception.exception.BadCredentialException;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.service.AppUserService;
import org.example.librarymanagement.service.GoogleAuthenticatorService;
import org.example.librarymanagement.service.ResetPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.ResourceBundle;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final ResourceBundle resourceBundle;
    private final BCryptPasswordEncoder passwordEncoder;
    private final GoogleAuthenticatorService googleAuthenticatorService;
    private final ResetPasswordService resetPasswordService;

    @Override
    public void verifyUser(AppUser user){
        user.setStatus(AccountStatus.ACTIVE);
        updateUser(user);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findAppUserByEmail(email);
    }

    @Override
    public void resetWrongLoginCounter(String email) {
        appUserRepository.resetWrongLoginCounter(email);
    }

    @Override
    public void requestResetPassword(ResetPasswordRequest request) {
        AppUser appUser = getAppUser(request.getEmail());
        if(!checkMatchingPassword(request, appUser.getPassword())){
            resetPasswordService.requestChangePassword(appUser, request);
        }else{
            throw new BadCredentialException("user.password.existed",
                    resourceBundle.getString("user.password.existed"));
        }
    }

    @Override
    public void resetPassword(String token) {
        AppUser appUser = resetPasswordService.confirmToken(token);
        updateUser(appUser);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {
        AppUser appUser = getAppUser(email);
        appUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        updateUser(appUser);
    }

    @Override
    public MfaResponse enableUserMfa(String email) {
        AppUser appUser = getAppUser(email);

        appUser.setMfa(true);
        appUser.setSecretKey(googleAuthenticatorService.generateSecretKey());
        updateUser(appUser);
        return new MfaResponse(googleAuthenticatorService.generateQRCode(appUser));
    }

    @Override
    public boolean isUserEnableMfa(String email){
        AppUser appUser = getAppUser(email);
        return appUser.getMfa();
    }

    @Override
    public boolean validateMfaOtp(String email, int otp) {
        AppUser appUser = getAppUser(email);
        return googleAuthenticatorService.validateTOTP(appUser, otp);
    }

    @Override
    public boolean checkMatchingPassword(ResetPasswordRequest request, String oldPassword) {
        return passwordEncoder.matches(request.getPassword(), oldPassword);
    }

    @Override
    public AppUser getAppUser(String email){
        return findByEmail(email)
                .orElseThrow(() -> new BadRequestException("user.email.email-not-found",
                        resourceBundle.getString("user.email.email-not-found")));
    }

    @Override
    public void saveUser(AppUser appUser){
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
    }

    @Override
    public void updateUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }
}
