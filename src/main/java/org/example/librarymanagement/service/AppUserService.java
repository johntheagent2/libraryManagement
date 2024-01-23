package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.*;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.dto.response.UserResponse;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface AppUserService {
    void verifyUser(AppUser user);

    Optional<AppUser> findByEmail(String email);

    AppUser getAppUser(String email);

    List<UserResponse> getAllUsers();

    List<UserResponse> getUsersWithCriteria(UserCriteriaRequest criteriaRequest);

    void resetWrongLoginCounter(String email);

    void requestResetPassword(ResetPasswordRequest request);

    void resetPassword(String token, String email);

    void changePassword(ChangePasswordRequest request);

    MfaResponse enableUserMfa();

    boolean isUserEnableMfa(String email);

    boolean validateMfaOtp(String email, int otp);

    boolean checkMatchingPassword(ResetPasswordRequest request, String oldPassword);

    void saveUser(AppUser appUser);

    void updateUser(AppUser appUser);

    void requestChangePhoneNumber(ChangePhoneNumberRequest request);

    void changePhoneNumber(OtpVerificationRequest request);

    void requestChangeEmail(ChangeEmailRequest request);

    void changeEmail(String token);

    UserDetails getCurrentLogin();
}
