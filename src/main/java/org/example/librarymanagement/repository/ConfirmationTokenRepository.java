package org.example.librarymanagement.repository;

import org.example.librarymanagement.model.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findConfirmationTokenByToken(String token);

    Optional<ConfirmationToken> findConfirmationTokenByOtp(String otp);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c SET c.expiresAt = ?2 WHERE c.token = ?1")
    void updateTokenExpiresByToken(String token, LocalDateTime newExpires);

    Optional<ConfirmationToken> findConfirmationTokenByAppUser_Email(String email);
}
