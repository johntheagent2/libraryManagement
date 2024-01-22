package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ConfirmationToken;
import org.example.librarymanagement.repository.ConfirmationTokenRepository;
import org.example.librarymanagement.service.ConfirmationTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final Random random = new Random();


    @Override
    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    @Override
    public ConfirmationToken createToken(AppUser appUser){
        String linkToken = createLinkToken();
        String otpToken = creatOTP();
        LocalDateTime createdDateTime = LocalDateTime.now();
        LocalDateTime expiresDateTime = createdDateTime.plusMinutes(15);

        return new ConfirmationToken(linkToken, otpToken, expiresDateTime, appUser);
    }

    @Override
    public ConfirmationToken refreshToken(ConfirmationToken token){
        LocalDateTime newExpires =  LocalDateTime.now().plusMinutes(15);

        token.setExpiresAt(newExpires);
        token.setToken(createLinkToken());
        token.setOtp(creatOTP());

        saveConfirmationToken(token);

        return token;
    }

    @Override
    public Optional<ConfirmationToken> findConfirmationToken(String token){
        return confirmationTokenRepository.findConfirmationTokenByToken(token);
    }

    @Override
    public Optional<ConfirmationToken> findConfirmationOTP(String otp){
        return confirmationTokenRepository.findConfirmationTokenByOtp(otp);
    }

    @Override
    public void setNewExpired(String token, LocalDateTime newExpires){
        confirmationTokenRepository.updateTokenExpiresByToken(token, newExpires);
    }

    @Override
    public void remove(ConfirmationToken token){
        confirmationTokenRepository.deleteById(token.getId());
    }

    @Override
    public Optional<ConfirmationToken> findConfirmationTokenByEmail(String email){
        return confirmationTokenRepository.findConfirmationTokenByAppUser_Email(email);
    }

    private String createLinkToken(){
        return UUID.randomUUID().toString();
    }

    private String creatOTP(){
        int otpLength = 6;

        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10)); // Append a random digit (0-9)
        }

        return otp.toString();
    }
}
