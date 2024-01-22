package org.example.librarymanagement.service;

import jakarta.transaction.Transactional;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.TokenOTP;
import org.example.librarymanagement.enumeration.ChangeType;

import java.time.LocalDateTime;

public interface TokenOtpService {
    void deleteDuplicateRequest(ChangeType type, String email);

    @Transactional
    void saveOtp(String request, AppUser appUser, ChangeType type);

    TokenOTP checkOtp(String tokenOtp, ChangeType type);

    void checkExpiration(LocalDateTime expirationDate);

    TokenOTP checkIfExist(String tokenOtp, ChangeType type);
}
