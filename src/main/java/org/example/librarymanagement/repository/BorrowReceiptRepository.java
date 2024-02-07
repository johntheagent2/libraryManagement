package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.BorrowReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BorrowReceiptRepository extends JpaRepository<BorrowReceipt, Long> {

    Optional<BorrowReceipt> getAllByAppUser_EmailAndActive(String email, boolean active);


    @Transactional
    @Modifying
    @Query("UPDATE BorrowReceipt br SET br.active = false WHERE br.id = ?1")
    void updateBorrowSession(Long id);
}
