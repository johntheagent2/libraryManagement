package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.ChangeEmailSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.management.LockInfo;
import java.util.Optional;

public interface ChangeEmailRepository extends JpaRepository<ChangeEmailSession, Long> {

    Optional<ChangeEmailSession> findByOldMail(String email);

    Optional<ChangeEmailSession> findByToken(String token);
}
