package org.example.librarymanagement.service.Imp;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ConfirmationToken;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.repository.ConfirmationTokenRepository;
import org.example.librarymanagement.service.ConfirmationTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public ConfirmationToken createToken(AppUser appUser){
        String linkToken = createLinkToken();
        String otpToken = creatOTP();
        LocalDateTime createdDateTime = LocalDateTime.now();
        LocalDateTime expiresDateTime = createdDateTime.plusMinutes(15);

        return new ConfirmationToken(linkToken, otpToken, createdDateTime, expiresDateTime, appUser);
    }

    public ConfirmationToken refreshToken(ConfirmationToken token){
        LocalDateTime newExpires =  LocalDateTime.now().plusMinutes(15);

        token.setExpiresAt(newExpires);
        token.setToken(createLinkToken());
        token.setOtp(creatOTP());

        saveConfirmationToken(token);

        return token;
    }

    public Optional<ConfirmationToken> findConfirmationToken(String token){
        return confirmationTokenRepository.findConfirmationTokenByToken(token);
    }

    public Optional<ConfirmationToken> findConfirmationOTP(String otp){
        return confirmationTokenRepository.findConfirmationTokenByOtp(otp);
    }

    public void setNewExpired(String token, LocalDateTime newExpires){
        confirmationTokenRepository.updateTokenExpiresByToken(token, newExpires);
    }

    public void remove(ConfirmationToken token){
        confirmationTokenRepository.deleteById(token.getId());
    }

    public Optional<ConfirmationToken> findConfirmationTokenByEmail(String email){
        return confirmationTokenRepository.findConfirmationTokenByAppUser_Email(email);
    }

    private String createLinkToken(){
        return UUID.randomUUID().toString();
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
