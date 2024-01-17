package org.example.librarymanagement.service.Imp;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.exception.exception.BadCredentialException;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.ResourceBundle;

@AllArgsConstructor
@Service
public class GoogleAuthenticatorServiceImpl {
    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
    private final ResourceBundle resourceBundle;

    public String generateSecretKey() {
        return googleAuthenticator.createCredentials().getKey();
    }

    public String generateQRCode(AppUser user) {
        GoogleAuthenticatorKey key = new GoogleAuthenticatorKey
                .Builder(user.getSecretKey())
                .setVerificationCode(0)
                .build();

        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("Sparkminds", user.getEmail(), key);
    }

    public boolean validateTOTP(AppUser user, int otp) {
        if(googleAuthenticator.authorize(user.getSecretKey(), otp)){
            return true;
        }else {
            throw new BadRequestException("google-authentication-otp-invalid",
                    resourceBundle.getString("google-authentication-otp-invalid"));
        }

    }
}
