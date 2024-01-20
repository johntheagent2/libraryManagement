package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.entity.AppUser;
//import org.example.librarymanagement.entity.ChangeEmailSession;
import org.example.librarymanagement.entity.SmsOtp;
import org.example.librarymanagement.exception.exception.BadRequestException;
//import org.example.librarymanagement.repository.ChangeEmailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChangeEmailServiceImpl {

//    private final ChangeEmailRepository changeEmailRepository;
//    private final ResourceBundle resourceBundle;
//
//    @Value("${email-link}")
//    private String link;
//
//    public void saveMailSession(String newEmail, AppUser appUser){
//        changeEmailRepository.save(new ChangeEmailSession(generateToken(),
//                newEmail,
//                LocalDateTime.now().plusMinutes(30),
//                appUser));
//    }
//
//    public void deleteDuplicateEmailRequest(String email){
//        changeEmailRepository.findByOldMail(email)
//                .ifPresent(emailSession -> changeEmailRepository.deleteById(emailSession.getId()));
//    }
//
//    public ChangeEmailSession findByToken(String token){
//        return changeEmailRepository.findByToken(token).orElseThrow(() -> new BadRequestException(
//                resourceBundle.getString("confirmation-token.otp.otp-not-found"),
//                "confirmation-token.otp.otp-not-found"));
//    }
//
//    public ChangeEmailSession checkToken(String token){
//        ChangeEmailSession emailSession = findByToken(token);
//
//        checkExpiration(emailSession);
//        changeEmailRepository.deleteById(emailSession.getId());
//
//        return emailSession;
//    }
//
//    public void checkExpiration(ChangeEmailSession emailSession){
//        if(emailSession.getExpirationDate().isBefore(LocalDateTime.now())){
//            throw new BadRequestException(
//                    resourceBundle.getString("confirmation-token.otp.otp-expired"),
//                    "confirmation-token.otp.otp-expired");
//        }
//    }
//
//    public String generateToken(){
//        return UUID.randomUUID().toString();
//    }
}
