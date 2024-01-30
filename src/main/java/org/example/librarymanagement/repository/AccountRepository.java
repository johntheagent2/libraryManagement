package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.base.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
