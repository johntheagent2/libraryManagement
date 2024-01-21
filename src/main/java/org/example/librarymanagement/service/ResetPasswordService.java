package org.example.librarymanagement.service;

import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ResetPasswordRequest;
import org.springframework.transaction.annotation.Transactional;

public interface ResetPasswordService {
    void requestChangePassword(AppUser appUser, org.example.librarymanagement.dto.request.ResetPasswordRequest request);

    String savePasswordSession(AppUser appUser, org.example.librarymanagement.dto.request.ResetPasswordRequest request);

    @Transactional
    AppUser confirmToken(String token);

    void verifyExpiryDate(ResetPasswordRequest resetPasswordRequest);

    void deletePasswordSession(Long id);

    void checkIfEmailRequestBefore(String email);

    ResetPasswordRequest getResetPasswordSessionOptional(String token);
}
