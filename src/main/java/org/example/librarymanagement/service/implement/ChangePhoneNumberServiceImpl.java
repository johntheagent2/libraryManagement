package org.example.librarymanagement.service.implement;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.common.sms.SmsSenderService;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ChangePhoneNumberRequest;
import org.example.librarymanagement.exception.exception.BadCredentialException;
import org.example.librarymanagement.repository.ChangePhoneNumberRepository;
import org.example.librarymanagement.service.ChangePhoneNumberService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;

@AllArgsConstructor
@Service
public class ChangePhoneNumberServiceImpl implements ChangePhoneNumberService {

    private final ChangePhoneNumberRepository changePhoneNumberRepository;
    private final ResourceBundle resourceBundle;
    private final SmsSenderService smsSenderService;
    @Override
    public void deleteDuplicatePhoneNumberRequest(String phoneNumber) {
        checkDuplicate(phoneNumber);
    }

    @Override
    @Transactional
    public void saveOtp(String newPhoneNumber, AppUser appUser) {
        String otp = OtpGenerator();
        String currentPhoneNumber = appUser.getPhoneNumber();
        ChangePhoneNumberRequest smsOtp = new ChangePhoneNumberRequest(
                otp,
                currentPhoneNumber,
                newPhoneNumber,
                LocalDateTime.now().plusSeconds(40),
                appUser);

//        smsSenderService.sendSms(smsOtp.getNewPhoneNumber(), otp);
        changePhoneNumberRepository.save(smsOtp);
    }

    @Override
    public void checkDuplicate(String phoneNumber){
        changePhoneNumberRepository.findByCurrentPhoneNumber(phoneNumber)
               .ifPresent(smsOtp -> changePhoneNumberRepository.deleteById(smsOtp.getId()));
    }

    @Override
    public ChangePhoneNumberRequest checkOtp(String otp) {
        ChangePhoneNumberRequest smsOtp = checkIfExist(otp);
        checkExpiration(smsOtp.getExpirationDate());

        return smsOtp;
    }

    @Override
    public void checkExpiration(LocalDateTime expirationDate){
        if(expirationDate.isBefore(LocalDateTime.now())){
            throw new BadCredentialException(
                    resourceBundle.getString("confirmation-token.otp.otp-expired"),
                    "confirmation-token.otp.otp-expired"
            );
        }
    }

    @Override
    public ChangePhoneNumberRequest checkIfExist(String otp){
        return changePhoneNumberRepository.findByToken(otp)
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
