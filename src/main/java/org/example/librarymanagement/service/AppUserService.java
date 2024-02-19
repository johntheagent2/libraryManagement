package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.*;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.dto.response.UserResponse;
import org.example.librarymanagement.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface AppUserService {

    void editUser(EditUserInfoRequest request, Long id);

    void verifyUser(AppUser user);

    void createUser(AdminCreateUserRequest request);

    Optional<AppUser> findByEmail(String email);

    void deleteUser(Long id);

    AppUser getAppUser(String email);

    List<UserResponse> getAllUsers();

    void requestResetPassword(ResetPasswordRequest request);

    MfaResponse enableUserMfa();

    void confirmMFA(String secretKey, String otp);

    boolean isUserEnableMfa(String email);

    boolean validateMfaOtp(String email, int otp);

    void saveUser(AppUser appUser);

    void updateUser(AppUser appUser);

    void requestChangePhoneNumber(ChangePhoneNumberRequest request);

    void requestChangeEmail(ChangeEmailRequest request);

    UserDetails getCurrentLogin();
}
