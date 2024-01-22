package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findAppUserByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser c SET c.enabled = TRUE WHERE c.email = ?1")
    void enableAppUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser c SET c.countWrongLogin = 0 WHERE c.email = ?1")
    void resetWrongLoginCounter(String email);

    boolean existsAppUserByEmail(String email);
}
