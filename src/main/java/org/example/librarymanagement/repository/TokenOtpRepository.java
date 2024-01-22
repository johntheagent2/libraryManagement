package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.TokenOTP;
import org.example.librarymanagement.enumeration.ChangeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenOtpRepository extends JpaRepository<TokenOTP, Long> {

    Optional<TokenOTP> findByTypeAndRequest(ChangeType type, String request);

    Optional<TokenOTP> findByOtpTokenAndType(String otp, ChangeType type);
}
