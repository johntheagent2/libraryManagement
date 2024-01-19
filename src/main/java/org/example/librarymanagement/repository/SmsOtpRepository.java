package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.SmsOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsOtpRepository extends JpaRepository<SmsOtp, Long> {

    Optional<SmsOtp> findByOtp(String otp);

    Optional<SmsOtp> findByCurrentPhoneNumber(String phoneNumber);
}
