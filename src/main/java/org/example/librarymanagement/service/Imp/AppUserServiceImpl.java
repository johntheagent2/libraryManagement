package org.example.librarymanagement.service.Imp;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.common.emailSender.EmailSenderService;
import org.example.librarymanagement.dto.response.UserDTO;
import org.example.librarymanagement.entity.ConfirmationToken;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.service.AppUserService;
import org.example.librarymanagement.service.ConfirmationTokenService;
import org.springframework.security.core.userdetails.User;
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
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final ResourceBundle resourceBundle;
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) appUserRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user.email.email-not-found",
                        resourceBundle.getString("user.email.email-not-found")));
    }

    @Transactional
    public void signupUser(AppUser appUser) throws MessagingException, TemplateException, IOException {
        boolean isUserExist = appUserRepository.findByEmail(appUser.getEmail())
                .isPresent();
        UserDTO userDTO;
        String token;
        String otp;

        if(isUserExist){
            throw new BadRequestException("user.email.email-existed",
                    resourceBundle.getString("user.email.email-existed"));
        }

        saveUser(appUser);
        token = createToken();
        otp = creatOTP();

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
    }

    public void resendVerification(ConfirmationToken token){
        LocalDateTime newExpires =  LocalDateTime.now().plusMinutes(15);
        String userEmail = token.getAppUser().getEmail();

        token.setExpiresAt(newExpires);
        token.setToken(createToken());
        token.setOtp(creatOTP());

        try{
            confirmationTokenService.saveConfirmationToken(token);
            emailSenderService.sendConfirmationMail(token.getToken(), token.getOtp(), userEmail);
        } catch (MessagingException | TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void enableAppUser(String email){
        appUserRepository.enableAppUser(email);
    }

    public void saveUser(AppUser appUser){
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
    }

    private String createToken(){
        String token = UUID.randomUUID().toString();

        return token;
    }

    private String creatOTP(){
        int otpLength = 6;

        Random random = new Random();
        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10)); // Append a random digit (0-9)
        }

        return otp.toString();
    }
}
