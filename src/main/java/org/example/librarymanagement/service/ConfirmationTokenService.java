package org.example.librarymanagement.service;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.model.token.ConfirmationToken;
import org.example.librarymanagement.repository.ConfirmationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
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

}
