package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.common.sms.SmsSenderService;
import org.example.librarymanagement.entity.SmsOtp;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.repository.SmsOtpRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;

@AllArgsConstructor
@Service
public class SmsOtpServiceImpl {

    private final SmsOtpRepository otpRepository;
    private final SmsSenderService smsSenderService;
    private final ResourceBundle resourceBundle;

    public void saveOtp(String oldPhoneNumber, String newPhoneNumber){
        String otp = generateOtp();
        otpRepository.save(new SmsOtp(otp, oldPhoneNumber, newPhoneNumber, LocalDateTime.now().plusSeconds(30)));
//        sendSMS(newPhoneNumber, otp);
    }

    public SmsOtp checkOtp(String otp){
        SmsOtp smsOtp = otpRepository.findByOtp(otp).orElseThrow(() -> new BadRequestException(
                resourceBundle.getString("confirmation-token.otp.otp-not-found"),
            "confirmation-token.otp.otp-not-found"
        ));

        checkExpiration(smsOtp);
        otpRepository.deleteById(smsOtp.getId());

        return smsOtp;
    }

    public void deleteDuplicatePhoneNumberRequest(String phoneNumber){
        otpRepository.findByCurrentPhoneNumber(phoneNumber)
                .ifPresent(smsOtp -> otpRepository.deleteById(smsOtp.getId()));
    }

    public void checkExpiration(SmsOtp smsOtp){
        if(smsOtp.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new BadRequestException(
                    resourceBundle.getString("confirmation-token.otp.otp-expired"),
                    "confirmation-token.otp.otp-expired");
        }
    }

    public void sendSMS(String newPhoneNumber, String otp){
        smsSenderService.sendSms(newPhoneNumber, otp);
    }

    public String generateOtp(){
        int otpLength = 6;
        StringBuilder otpBuilder = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < otpLength; i++) {
            int digit = random.nextInt(10);
            otpBuilder.append(digit);
        }

        return otpBuilder.toString();
    }
}
