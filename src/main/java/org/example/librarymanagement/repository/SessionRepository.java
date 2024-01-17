package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> getSessionByJti(String jti);

    @Transactional
    @Modifying
    @Query("UPDATE Session s SET s.isActive = false WHERE s.jti = ?1")
    void deactivateSession(String jti);
}
