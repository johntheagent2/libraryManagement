package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.ResetPasswordRequest;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ResetPasswordSession;
import org.springframework.transaction.annotation.Transactional;

public interface ResetPasswordService {
    void requestChangePassword(AppUser appUser, ResetPasswordRequest request);

    String savePasswordSession(AppUser appUser, ResetPasswordRequest request);

    @Transactional
    AppUser confirmToken(String token);

    void verifyExpiryDate(ResetPasswordSession resetPasswordSession);

    void deletePasswordSession(Long id);

    void checkIfEmailRequestBefore(String email);

    ResetPasswordSession getResetPasswordSessionOptional(String token);
}
