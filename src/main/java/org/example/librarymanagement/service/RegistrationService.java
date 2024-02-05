package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.RegistrationRequest;
import org.example.librarymanagement.entity.ConfirmationToken;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationService {
    void register(RegistrationRequest request);

    void confirmToken(String token);

    void confirmOtpToken(String otp);

    void verifyExpiryDate(ConfirmationToken confirmationToken);

    void resendToken(String email);
}
