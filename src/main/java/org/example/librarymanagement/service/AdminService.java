package org.example.librarymanagement.service;

import org.example.librarymanagement.entity.Admin;
import org.example.librarymanagement.entity.AppUser;

import java.util.Optional;

public interface AdminService {
    Admin saveAdmin(Admin admin);

    void updateUser(Admin admin);

    Optional<Admin> findByEmail(String email);
}
