package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
