package org.example.librarymanagement.common;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

@AllArgsConstructor
public class Global {
    public static String otpGenerator(){
        StringBuilder otp = new StringBuilder();
        Random random = new Random();

        // Generate 6 random digits
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }

    public static String UUIDgenrator(){
        return UUID.randomUUID().toString();
    }
}
