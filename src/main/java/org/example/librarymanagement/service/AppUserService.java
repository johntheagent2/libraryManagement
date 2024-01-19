package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.ChangePasswordRequest;
import org.example.librarymanagement.dto.request.ResetPasswordRequest;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.exception.exception.BadRequestException;

import java.util.Optional;

public interface AppUserService {
    void verifyUser(AppUser user);

    Optional<AppUser> findByEmail(String email);

    AppUser getAppUser(String email);

    void resetWrongLoginCounter(String email);

    void requestResetPassword(ResetPasswordRequest request);

    void resetPassword(String token);

    void changePassword(String email, ChangePasswordRequest request);

    MfaResponse enableUserMfa(String email);

    boolean isUserEnableMfa(String email);

    boolean validateMfaOtp(String email, int otp);

    boolean checkMatchingPassword(ResetPasswordRequest request, String oldPassword);

    void saveUser(AppUser appUser);

    void updateUser(AppUser appUser);
}
