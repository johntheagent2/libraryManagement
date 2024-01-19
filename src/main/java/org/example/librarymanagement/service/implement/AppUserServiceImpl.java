package org.example.librarymanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.common.sms.SmsSenderService;
import org.example.librarymanagement.dto.request.*;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ChangeEmailSession;
import org.example.librarymanagement.entity.SmsOtp;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.exception.exception.BadCredentialException;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.service.AppUserService;
import org.example.librarymanagement.service.GoogleAuthenticatorService;
import org.example.librarymanagement.service.ResetPasswordService;
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
    private final ResetPasswordService resetPasswordService;
    private final SmsOtpServiceImpl smsOtpService;
    private final ChangeEmailServiceImpl changeEmailService;

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

    @Override
    public void requestChangePhoneNumber(String email, ChangePhoneNumberRequest request) {
        AppUser appUser = getAppUser(email);

        if(appUser.getPhoneNumber().equals(request.getPhoneNumber())){
            throw new BadRequestException(resourceBundle.getString("user.phone.phone-duplicate"),
                    "user.phone.phone-duplicate");
        }
        smsOtpService.deleteDuplicatePhoneNumberRequest(appUser.getPhoneNumber());
        smsOtpService.saveOtp(appUser.getPhoneNumber(), request.getPhoneNumber());
    }

    @Transactional
    @Override
    public void changePhoneNumber(String email, OtpVerificationRequest request) {
        AppUser appUser;
        SmsOtp smsOtp = smsOtpService.checkOtp(request.getOtp());

        appUser = getAppUser(email);
        appUser.setPhoneNumber(smsOtp.getNewPhoneNumber());

        updateUser(appUser);
    }

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
        changeEmailService.deleteDuplicateEmailRequest(email);
        changeEmailService.saveMailSession(request.getEmail(), appUser);

    }

    @Override
    public void changeEmail(String token) {
        AppUser appUser;
        ChangeEmailSession changeEmailSession = changeEmailService.checkToken(token);

        appUser = changeEmailSession.getAppUser();
        appUser.setEmail(changeEmailSession.getNewEmail());
        updateUser(appUser);
    }
}
