package org.example.librarymanagement.common;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

@AllArgsConstructor
public class Global {

    public static String otpGenerator() {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();

        // Generate 6 random digits
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }

    public static String UUIDgenrator() {
        return UUID.randomUUID().toString();
    }

    public static UserDetails getCurrentLogin(ResourceBundle resourceBundle) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return (UserDetails) authentication.getPrincipal();
        } else {
            throw new BadRequestException("security.core.userdetails", resourceBundle.getString("security.core.userdetails"));
        }
    }
}
