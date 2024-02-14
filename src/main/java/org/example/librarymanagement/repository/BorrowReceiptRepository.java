package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.BorrowReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BorrowReceiptRepository extends JpaRepository<BorrowReceipt, Long> {

    Optional<BorrowReceipt> getAllByAppUser_EmailAndActive(String email, boolean active);

    @Query("SELECT CASE WHEN COUNT(br) > 0 THEN true ELSE false END " +
            "FROM BorrowReceipt br " +
            "WHERE br.appUser.email = ?1 and br.active = ?2")
    boolean existsByEmailAndActive(String email, boolean isActive);

    @Transactional
    @Modifying
    @Query("UPDATE BorrowReceipt br " +
            "SET br.active = false " +
            "WHERE EXISTS (SELECT 1 FROM AppUser au " +
            "              WHERE au.id = br.appUser.id " +
            "                AND au.email = ?1 " +
            "                AND br.active = ?2)")
    void updateBorrowSession(String email, boolean isActive);
}
