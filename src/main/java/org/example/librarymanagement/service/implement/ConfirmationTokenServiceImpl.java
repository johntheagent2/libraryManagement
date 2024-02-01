package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.common.Global;
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
        String linkToken = Global.UUIDgenrator();
        String otpToken = Global.otpGenerator();
        LocalDateTime createdDateTime = LocalDateTime.now();
        LocalDateTime expiresDateTime = createdDateTime.plusMinutes(15);

        return new ConfirmationToken(linkToken, otpToken, expiresDateTime, appUser);
    }

    @Override
    public ConfirmationToken refreshToken(ConfirmationToken token){
        LocalDateTime newExpires =  LocalDateTime.now().plusMinutes(15);

        token.setExpiresAt(newExpires);
        token.setToken(Global.UUIDgenrator());
        token.setOtp(Global.otpGenerator());

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
}
