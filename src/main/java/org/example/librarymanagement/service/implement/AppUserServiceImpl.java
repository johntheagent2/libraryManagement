package org.example.librarymanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.*;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.TokenOTP;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.enumeration.ChangeType;
import org.example.librarymanagement.exception.exception.BadCredentialException;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.service.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.ResourceBundle;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final ResourceBundle resourceBundle;
    private final BCryptPasswordEncoder passwordEncoder;
    private final GoogleAuthenticatorService googleAuthenticatorService;
    private final TokenOtpService tokenOtpService;
    private final ChangeType resetPassword = ChangeType.RESET_PASSWORD;
    private final ChangeType changeEmail = ChangeType.CHANGE_EMAIL;
    private final ChangeType changePhoneNumber = ChangeType.CHANGE_PHONE_NUMBER;

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
    @Transactional
    public void requestResetPassword(ResetPasswordRequest request) {
        AppUser appUser = getAppUser(request.getEmail());
        if(!checkMatchingPassword(request, appUser.getPassword())){
            tokenOtpService.deleteDuplicateRequest(resetPassword, request.getPassword());
            tokenOtpService.saveOtp(request.getPassword(), appUser, resetPassword);
        }else{
            throw new BadCredentialException("user.password.existed",
                    resourceBundle.getString("user.password.existed"));
        }
    }

    @Transactional
    @Override
    public void resetPassword(String token) {
        AppUser appUser;
        TokenOTP tokenOTP = tokenOtpService.checkOtp(token, resetPassword);

        appUser = tokenOTP.getAppUser();
        appUser.setPassword(passwordEncoder.encode(tokenOTP.getRequest()));

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

    @Transactional
    @Override
    public void requestChangePhoneNumber(String email, ChangePhoneNumberRequest request) {
        AppUser appUser = getAppUser(email);
        String currentPhoneNumber = appUser.getPhoneNumber();

        if(currentPhoneNumber.equals(request.getPhoneNumber())){
            throw new BadRequestException(resourceBundle.getString("user.phone.phone-duplicate"),
                    "user.phone.phone-duplicate");
        }
        tokenOtpService.deleteDuplicateRequest(changePhoneNumber, request.getPhoneNumber());
        tokenOtpService.saveOtp(request.getPhoneNumber(), appUser, changePhoneNumber);
    }

    @Transactional
    @Override
    public void changePhoneNumber(String email, OtpVerificationRequest request) {
        AppUser appUser;
        TokenOTP tokenOTP = tokenOtpService.checkOtp(request.getOtp(), changePhoneNumber);

        appUser = tokenOTP.getAppUser();
        appUser.setPhoneNumber(tokenOTP.getRequest());

        updateUser(appUser);
    }

    @Transactional
    @Override
    public void requestChangeEmail(String email, ChangeEmailRequest request) {
        AppUser appUser;
        if(email.equals(request.getEmail())){
            throw new BadRequestException(resourceBundle.getString("user.email.email-duplicate"),
                    "user.email.email-duplicate");
        }

        if(findByEmail(request.getEmail()).isPresent()){
            throw new BadRequestException("user.email.email-existed",
                    resourceBundle.getString("user.email.email-existed"));
        }

        appUser = getAppUser(email);
        tokenOtpService.deleteDuplicateRequest(changeEmail, request.getEmail());
        tokenOtpService.saveOtp(request.getEmail(), appUser, changeEmail);

    }

    @Transactional
    @Override
    public void changeEmail(String token) {
        AppUser appUser;
        TokenOTP tokenOTP = tokenOtpService.checkOtp(token, changeEmail);

        appUser = tokenOTP.getAppUser();
        appUser.setEmail(tokenOTP.getOtpToken());
        updateUser(appUser);
    }
}
