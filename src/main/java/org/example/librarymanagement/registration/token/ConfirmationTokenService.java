package org.example.librarymanagement.registration.token;

import lombok.AllArgsConstructor;
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

    public int setConfirmationTokenAt(String token){
        return confirmationTokenRepository.updateConfirmationTokenByToken(token, LocalDateTime.now());
    }

}
