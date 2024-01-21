package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.ResetPasswordRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordRepository extends JpaRepository<ResetPasswordRequest, Long> {

    Optional<ResetPasswordRequest> findResetPasswordSessionByToken(String token);

    Optional<ResetPasswordRequest> findByToken(String token);

    Optional<ResetPasswordRequest> findByAppUser_Email(String email);

}
