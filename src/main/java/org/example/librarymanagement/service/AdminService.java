package org.example.librarymanagement.service;

import org.example.librarymanagement.entity.Admin;

import java.util.Optional;

public interface AdminService {

    Optional<Admin> findByEmail(String email);
}
