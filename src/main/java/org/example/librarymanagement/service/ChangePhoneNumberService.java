package org.example.librarymanagement.service;

import jakarta.transaction.Transactional;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ChangePhoneNumber;

import java.time.LocalDateTime;

public interface ChangePhoneNumberService {
    void deleteDuplicatePhoneNumberRequest(String phoneNumber);

    @Transactional
    void saveOtp(String newPhoneNumber, AppUser appUser);

    void checkDuplicate(String phoneNumber);

    ChangePhoneNumber checkOtp(String otp);

    void checkExpiration(LocalDateTime expirationDate);

    ChangePhoneNumber checkIfExist(String otp);
}
