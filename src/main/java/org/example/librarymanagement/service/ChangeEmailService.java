package org.example.librarymanagement.service;

import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ChangeEmailRequest;

public interface ChangeEmailService {
    void saveMailSession(String newEmail, AppUser appUser);

    void deleteDuplicateEmailRequest(String email);

    ChangeEmailRequest findByToken(String token);

    ChangeEmailRequest checkToken(String token);

    void checkExpiration(ChangeEmailRequest emailSession);

    String generateToken();
}
