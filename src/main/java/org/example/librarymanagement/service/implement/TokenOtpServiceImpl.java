package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.common.email.EmailSenderService;
import org.example.librarymanagement.common.sms.SmsSenderService;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.TokenOTP;
import org.example.librarymanagement.enumeration.ChangeType;
import org.example.librarymanagement.exception.exception.BadCredentialException;
import org.example.librarymanagement.repository.TokenOtpRepository;
import org.example.librarymanagement.service.TokenOtpService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

@AllArgsConstructor
@Service
public class TokenOtpServiceImpl implements TokenOtpService {

    private final TokenOtpRepository tokenOtpRepository;
    private final ResourceBundle resourceBundle;
    private final SmsSenderService smsSenderService;
    private final EmailSenderService emailSenderService;
//    tokenOTP -> tokenOtpRepository.deleteById(tokenOTP.getId())
    @Override
    public void deleteDuplicateRequest(ChangeType type, String request) {
        System.out.println("Start");
        tokenOtpRepository.findByTypeAndRequest(type, request)
                .ifPresent((tokenOTP -> {
                    tokenOTP.getAppUser().removeTokenOTP(tokenOTP);
                    delete(tokenOTP.getId());
                }));  
    }

    public void delete(Long id){
        tokenOtpRepository.deleteById(id);
    }

    @Override
    public void saveOtp(String request, AppUser appUser, ChangeType type) {
        String tokenOtp = generateBasedOnType(type);

        TokenOTP smsOtp = new TokenOTP(
                tokenOtp,
                request,
                type,
                LocalDateTime.now().plusSeconds(40),
                appUser);

//        sendTokenOTP(tokenOtp, request, type);
        tokenOtpRepository.save(smsOtp);
    }

    @Override
    public TokenOTP checkOtp(String tokenOtp, ChangeType type) {
        TokenOTP smsOtp = checkIfExist(tokenOtp, type);
        checkExpiration(smsOtp.getExpirationDate());

        return smsOtp;
    }

    @Override
    public void checkExpiration(LocalDateTime expirationDate) {
        if(expirationDate.isBefore(LocalDateTime.now())){
            throw new BadCredentialException(
                    resourceBundle.getString("confirmation-token.otp.otp-expired"),
                    "confirmation-token.otp.otp-expired"
            );
        }
    }

    @Override
    public TokenOTP checkIfExist(String tokenOtp, ChangeType type) {
        return tokenOtpRepository.findByOtpTokenAndType(tokenOtp, type)
                .orElseThrow(() -> new BadCredentialException(
                        resourceBundle.getString("confirmation-token.otp.otp-not-found"),
                        "confirmation-token.otp.otp-not-found"));
    }

    private String tokenGenerator(){
        return UUID.randomUUID().toString();
    }

    private String otpGenerator(){
        int otpLength = 6;
        String allowedChars = "0123456789";

        StringBuilder otp = new StringBuilder(otpLength);

        Random random = new Random();

        for (int i = 0; i < otpLength; i++) {
            int index = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(index);
            otp.append(randomChar);
        }

        return otp.toString();
    }

    public String generateBasedOnType(ChangeType type){
        if(type.equals(ChangeType.CHANGE_EMAIL) || type.equals(ChangeType.RESET_PASSWORD)){
            return tokenGenerator();
        }else {
            return otpGenerator();
        }
    }

    private void sendTokenOTP(String tokenOtp, String request, ChangeType type) {
        if(type.equals(ChangeType.CHANGE_EMAIL) || type.equals(ChangeType.RESET_PASSWORD)){
            emailSenderService.sendChangeRequest(tokenOtp, request);
        }else {
            smsSenderService.sendSms(request, tokenOtp);
        }
    }
}
