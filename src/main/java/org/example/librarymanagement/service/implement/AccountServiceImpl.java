package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.common.Global;
import org.example.librarymanagement.config.security.PasswordEncoder;
import org.example.librarymanagement.dto.request.*;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.TokenOTP;
import org.example.librarymanagement.entity.base.Account;
import org.example.librarymanagement.enumeration.ChangeType;
import org.example.librarymanagement.exception.exception.BadCredentialException;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.repository.AccountRepository;
import org.example.librarymanagement.service.AccountService;
import org.example.librarymanagement.service.GoogleAuthenticatorService;
import org.example.librarymanagement.service.SessionService;
import org.example.librarymanagement.service.TokenOtpService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.ResourceBundle;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ResourceBundle resourceBundle;
    private final TokenOtpService tokenOtpService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final ChangeType resetPassword = ChangeType.RESET_PASSWORD;
    private final ChangeType changeEmail = ChangeType.CHANGE_EMAIL;
    private final ChangeType changePhoneNumber = ChangeType.CHANGE_PHONE_NUMBER;

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Override
    public void resetWrongLoginCounter(String email) {
        accountRepository.resetWrongLoginCounter(email);
    }

    @Override
    public void resetPassword(String token, String email) {
        Account account;
        TokenOTP tokenOTP = tokenOtpService.checkOtp(token, resetPassword, email);

        account = tokenOTP.getAppUser();
        account.setPassword(passwordEncoder.encode(tokenOTP.getRequest()));

        updateAccount(account);
        tokenOtpService.deleteById(tokenOTP.getId());
    }

    @Transactional
    @Override
    public void changePassword(ChangePasswordRequest request) {
        String email = Global.getCurrentLogin(resourceBundle).getUsername();
        Account account = getAccount(email);
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        updateAccount(account);
        sessionService.deactivateSession();
    }

    @Override
    public boolean checkMatchingPassword(ResetPasswordRequest request, String oldPassword) {
        return passwordEncoder.matches(request.getPassword(), oldPassword);
    }

    @Transactional
    @Override
    public void changePhoneNumber(OtpVerificationRequest request) {
        String email = Global.getCurrentLogin(resourceBundle).getUsername();
        Account account;
        TokenOTP tokenOTP = tokenOtpService.checkOtp(request.getOtp(), changePhoneNumber, email);

        account = tokenOTP.getAppUser();
        account.setPhoneNumber(tokenOTP.getRequest());

        updateAccount(account);
        tokenOtpService.deleteById(tokenOTP.getId());
    }

    @Transactional
    @Override
    public void changeEmail(String token) {
        String email = Global.getCurrentLogin(resourceBundle).getUsername();
        Account account;
        TokenOTP tokenOTP = tokenOtpService.checkOtp(token, changeEmail, email);

        account = tokenOTP.getAppUser();
        account.setEmail(tokenOTP.getRequest());
        updateAccount(account);
        sessionService.deactivateSession();
        tokenOtpService.deleteById(tokenOTP.getId());
    }

    public void updateAccount(Account account){
        accountRepository.save(account);
    }

    public Account getAccount(String email){
        return findByEmail(email)
                .orElseThrow(() -> new BadRequestException("user.email.email-not-found",
                        resourceBundle.getString("user.email.email-not-found")));
    }
}
