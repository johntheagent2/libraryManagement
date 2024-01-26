package org.example.librarymanagement.service.implement;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@AllArgsConstructor
@Service
public class GoogleAuthenticatorServiceImpl implements org.example.librarymanagement.service.GoogleAuthenticatorService {
    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
    private final ResourceBundle resourceBundle;

    @Override
    public String generateSecretKey() {
        return googleAuthenticator.createCredentials().getKey();
    }

    @Override
    public String generateQRCode(String email, String secretKey) {
        GoogleAuthenticatorKey key = new GoogleAuthenticatorKey
                .Builder(secretKey)
                .setVerificationCode(0)
                .build();

        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("Sparkminds", email, key);
    }

    @Override
    public boolean validateSecretKey(String secretKey, int otp) {
        if(googleAuthenticator.authorize(secretKey, otp)){
            return true;
        }else {
            throw new BadRequestException("google-authentication-secret-key-invalid",
                    resourceBundle.getString("google-authentication-secret-key-invalid"));
        }

    }

    @Override
    public boolean validateTOTP(AppUser user, int otp) {
        if(googleAuthenticator.authorize(user.getSecretKey(), otp)){
            return true;
        }else {
            throw new BadRequestException("google-authentication-otp-invalid",
                    resourceBundle.getString("google-authentication-otp-invalid"));
        }

    }
}
