package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.ChangePhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChangePhoneNumberRepository extends JpaRepository<ChangePhoneNumber, Long> {

    Optional<ChangePhoneNumber> findByToken(String otp);

    Optional<ChangePhoneNumber> findByCurrentPhoneNumber(String phoneNumber);
}
