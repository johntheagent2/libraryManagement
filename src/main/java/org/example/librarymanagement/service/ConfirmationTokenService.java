package org.example.librarymanagement.service;

import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ConfirmationToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken token);

    ConfirmationToken createToken(AppUser appUser);

    ConfirmationToken refreshToken(ConfirmationToken token);

    Optional<ConfirmationToken> findConfirmationToken(String token);

    Optional<ConfirmationToken> findConfirmationOTP(String otp);

    void setNewExpired(String token, LocalDateTime newExpires);

    void remove(ConfirmationToken token);

    Optional<ConfirmationToken> findConfirmationTokenByEmail(String email);
}
