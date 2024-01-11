package org.example.librarymanagement.service;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ConfirmationToken;

import java.io.IOException;

public interface AppUserService {

    void signupUser(AppUser appUser) throws MessagingException, TemplateException, IOException;

    void enableAppUser(String email);

    void resendVerification(ConfirmationToken token);

    void saveUser(AppUser appUser);
}
