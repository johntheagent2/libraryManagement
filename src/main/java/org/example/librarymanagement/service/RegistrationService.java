package org.example.librarymanagement.service;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.example.librarymanagement.dto.request.RegistrationDTO;
import org.example.librarymanagement.entity.ConfirmationToken;

import java.io.IOException;

public interface RegistrationService {

    void register(RegistrationDTO request) throws MessagingException, TemplateException, IOException;

    void confirmToken(String token);

    void confirmOtpToken(String otp);

    void verifyExpiryDate(ConfirmationToken confirmationToken);

    void resendToken(String email);
}
