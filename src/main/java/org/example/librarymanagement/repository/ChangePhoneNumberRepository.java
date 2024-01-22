package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.ChangePhoneNumberRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChangePhoneNumberRepository extends JpaRepository<ChangePhoneNumberRequest, Long> {

    Optional<ChangePhoneNumberRequest> findByToken(String otp);

    Optional<ChangePhoneNumberRequest> findByCurrentPhoneNumber(String phoneNumber);
}
