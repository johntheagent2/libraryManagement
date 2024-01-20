package org.example.librarymanagement.service.implement;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.common.sms.SmsSenderService;
import org.example.librarymanagement.entity.SmsOtp;
import org.example.librarymanagement.exception.exception.BadCredentialException;
import org.example.librarymanagement.repository.SmsOtpRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;

@AllArgsConstructor
@Service
public class SmsOtpServiceImpl {

    private final SmsOtpRepository smsOtpRepository;
    private final ResourceBundle resourceBundle;
    private final SmsSenderService smsSenderService;
    public void deleteDuplicatePhoneNumberRequest(String phoneNumber) {
        checkDuplicate(phoneNumber);
    }

    @Transactional
    public void saveOtp(String currentPhoneNumber, String newPhoneNumber) {
        String otp = OtpGenerator();
        SmsOtp smsOtp = new SmsOtp(
                otp,
                currentPhoneNumber,
                newPhoneNumber,
                LocalDateTime.now().plusSeconds(40));

//        smsSenderService.sendSms(smsOtp.getNewPhoneNumber(), otp);
        smsOtpRepository.save(smsOtp);
    }

    public void checkDuplicate(String phoneNumber){
       smsOtpRepository.findByCurrentPhoneNumber(phoneNumber)
               .ifPresent(smsOtp -> smsOtpRepository.deleteById(smsOtp.getId()));
    }

    public SmsOtp checkOtp(String otp) {
        SmsOtp smsOtp = checkIfExist(otp);
        checkExpiration(smsOtp.getExpirationDate());

        return smsOtp;
    }

    public void checkExpiration(LocalDateTime expirationDate){
        if(expirationDate.isBefore(LocalDateTime.now())){
            throw new BadCredentialException(
                    resourceBundle.getString("confirmation-token.otp.otp-expired"),
                    "confirmation-token.otp.otp-expired"
            );
        }
    }

    public SmsOtp checkIfExist(String otp){
        return smsOtpRepository.findByOtp(otp)
                .orElseThrow(() -> new BadCredentialException(
                        resourceBundle.getString("confirmation-token.otp.otp-not-found"),
                        "confirmation-token.otp.otp-not-found"));
    }

    private String OtpGenerator(){
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
}
