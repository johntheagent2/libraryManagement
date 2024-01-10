package org.example.librarymanagement.service;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.common.emailSender.EmailSenderService;
import org.example.librarymanagement.config.message.MessageConfig;
import org.example.librarymanagement.dto.UserDTO;
import org.example.librarymanagement.model.account.appUser.AppUser;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.model.token.ConfirmationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AppUserService implements UserDetailsService{

    private final String USER_NOT_FOUND_MESSAGE = "User with email %s is not found";
    private final ResourceBundle messageConfig;
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException(messageConfig.getString("user.email.email-not-found")));
    }

    @Transactional
    public UserDTO signupUser(AppUser appUser) throws MessagingException, TemplateException, IOException {
        boolean isUserExist = appUserRepository.findByEmail(appUser.getEmail())
                .isPresent();
        UserDTO userDTO;
        String token;
        String otp;

        if(isUserExist){
            throw new IllegalStateException("Email is registered already!");
        }

        createUser(appUser);
        token = createToken(appUser);
        otp = creatOTP(appUser);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                otp,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                appUser);

        userDTO = UserDTO.builder()
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .address(appUser.getAddress())
                .phoneNumber(appUser.getPhoneNumber())
                .email(appUser.getEmail())
                .build();

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        emailSenderService.sendConfirmationMail(token, otp, appUser.getEmail());

        return userDTO;
    }

    public void enableAppUser(String email){
        appUserRepository.enableAppUser(email);
    }

    private void createUser(AppUser appUser){
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
    }

    private String createToken(AppUser appUser){
        String token = UUID.randomUUID().toString();

        return token;
    }

    private String creatOTP(AppUser appUser){
        int otpLength = 6;

        Random random = new Random();
        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10)); // Append a random digit (0-9)
        }

        return otp.toString();
    }
}
