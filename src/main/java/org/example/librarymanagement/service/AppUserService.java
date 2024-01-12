package org.example.librarymanagement.service;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ConfirmationToken;

import java.io.IOException;
import java.util.Optional;

public interface AppUserService {

    void saveUser(AppUser appUser);

    void enableAppUser(String email);

    Optional<AppUser> findByEmail(String email);
}
