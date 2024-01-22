package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.common.email.EmailSenderService;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ChangeEmailRequest;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.repository.ChangeEmailRepository;
import org.example.librarymanagement.service.ChangeEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChangeEmailServiceImpl implements ChangeEmailService {

    private final ChangeEmailRepository changeEmailRepository;
    private final ResourceBundle resourceBundle;
    private final EmailSenderService emailSenderService;

    @Override
    public void saveMailSession(String newEmail, AppUser appUser){
        String token = generateToken();

//        emailSenderService.sendChangeRequest(token, newEmail);
        changeEmailRepository.save(new ChangeEmailRequest(token,
                appUser.getEmail(),
                newEmail,
                LocalDateTime.now().plusMinutes(30),
                appUser));
    }

    @Override
    public void deleteDuplicateEmailRequest(String email){
        changeEmailRepository.findByCurrentEmail(email)
                .ifPresent(emailSession -> changeEmailRepository.deleteById(emailSession.getId()));
    }

    @Override
    public ChangeEmailRequest findByToken(String token){
        return changeEmailRepository.findByToken(token).orElseThrow(() -> new BadRequestException(
                resourceBundle.getString("confirmation-token.otp.otp-not-found"),
                "confirmation-token.otp.otp-not-found"));
    }

    @Override
    public ChangeEmailRequest checkToken(String token){
        ChangeEmailRequest emailSession = findByToken(token);

        checkExpiration(emailSession);
        changeEmailRepository.deleteById(emailSession.getId());

        return emailSession;
    }

    @Override
    public void checkExpiration(ChangeEmailRequest emailSession){
        if(emailSession.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new BadRequestException(
                    resourceBundle.getString("confirmation-token.otp.otp-expired"),
                    "confirmation-token.otp.otp-expired");
        }
    }

    @Override
    public String generateToken(){
        return UUID.randomUUID().toString();
    }
}
