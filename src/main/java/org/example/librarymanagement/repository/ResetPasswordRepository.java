package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.ResetPasswordSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ResetPasswordRepository extends JpaRepository<ResetPasswordSession, Long> {

    Optional<ResetPasswordSession> findResetPasswordSessionByToken(String token);

    Optional<ResetPasswordSession> findByToken(String token);

    Optional<ResetPasswordSession> findByAppUser_Email(String email);

}
