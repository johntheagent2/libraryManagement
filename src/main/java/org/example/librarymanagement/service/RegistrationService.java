package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.RegistrationRequest;
import org.example.librarymanagement.entity.ConfirmationToken;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationService {
    @Transactional
    void register(RegistrationRequest request);

    @Transactional
    void confirmToken(String token);

    @Transactional
    void confirmOtpToken(String otp);

    void verifyExpiryDate(ConfirmationToken confirmationToken);

    void resendToken(String email);
}
