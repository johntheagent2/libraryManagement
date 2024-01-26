package org.example.librarymanagement.service;

import org.example.librarymanagement.entity.AppUser;

public interface GoogleAuthenticatorService {
    String generateSecretKey();

    String generateQRCode(String email, String secretKey);

    boolean validateTOTP(AppUser user, int otp);

    boolean validateSecretKey(String secretKey, int otp);

}
