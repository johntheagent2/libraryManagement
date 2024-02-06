package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.BorrowReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowReceiptRepository extends JpaRepository<BorrowReceipt, Long> {

    Optional<BorrowReceipt> getAllByAppUser_EmailAndActive(String email, boolean active);
}
