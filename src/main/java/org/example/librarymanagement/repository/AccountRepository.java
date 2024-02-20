package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.base.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.enabled = TRUE WHERE a.email = ?1")
    void enableAccount(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.countWrongLogin = 0 WHERE a.email = ?1")
    void resetWrongLoginCounter(String email);
}
