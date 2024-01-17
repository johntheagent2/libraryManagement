package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> getSessionByJti(String jti);
}
