package org.example.librarymanagement.service;

import org.example.librarymanagement.entity.AppUser;

public interface GoogleAuthenticatorService {
    String generateSecretKey();

    String generateQRCode(AppUser user);

    boolean validateTOTP(AppUser user, int otp);
}
