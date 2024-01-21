package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.ChangeEmailRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.management.LockInfo;
import java.util.Optional;

public interface ChangeEmailRepository extends JpaRepository<ChangeEmailRequest, Long> {

    Optional<ChangeEmailRequest> findByCurrentEmail(String email);

    Optional<ChangeEmailRequest> findByToken(String token);
}
