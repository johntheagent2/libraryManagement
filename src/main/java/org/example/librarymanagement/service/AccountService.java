package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.ChangePasswordRequest;
import org.example.librarymanagement.dto.request.OtpVerificationRequest;
import org.example.librarymanagement.dto.request.ResetPasswordRequest;
import org.example.librarymanagement.entity.base.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> findByEmail(String email);

    List<String> getAllEmail();

    void resetWrongLoginCounter(String email);

    void resetPassword(String token, String email);

    void changePassword(ChangePasswordRequest request);

    boolean checkMatchingPassword(ResetPasswordRequest request, String oldPassword);

    void changePhoneNumber(OtpVerificationRequest request);

    void changeEmail(String token);
}
