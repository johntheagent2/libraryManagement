package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.*;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.base.Account;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AccountService {
    Optional<Account> findByEmail(String email);

    void resetWrongLoginCounter(String email);

    void resetPassword(String token, String email);

    void changePassword(ChangePasswordRequest request);

    boolean checkMatchingPassword(ResetPasswordRequest request, String oldPassword);

    void changePhoneNumber(OtpVerificationRequest request);

    void changeEmail(String token);

    UserDetails getCurrentLogin();
}
