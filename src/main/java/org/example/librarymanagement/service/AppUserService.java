package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.ResetPasswordRequest;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.entity.AppUser;

import java.util.Optional;

public interface AppUserService {
    void verifyUser(AppUser user);

    Optional<AppUser> findByEmail(String email);

    void resetWrongLoginCounter(String email);

    void resetPassword(ResetPasswordRequest request);

    MfaResponse enableUserMfa(String email);

    boolean isUserEnableMfa(String email);

    boolean validateMfaOtp(String email, int otp);

    boolean checkMatchingPassword(ResetPasswordRequest request);

    void saveUser(AppUser appUser);

    void updateUser(AppUser appUser);
}
