package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.TokenOTP;
import org.example.librarymanagement.enumeration.ChangeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenOtpRepository extends JpaRepository<TokenOTP, Long> {

    Optional<TokenOTP> findByTypeAndRequest(ChangeType type, String request);

    TokenOTP findByAppUser_Email(String email);

    Optional<TokenOTP> findByOtpTokenAndTypeAndAppUser_Email(String otp, ChangeType type, String email);
}
