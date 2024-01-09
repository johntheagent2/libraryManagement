package org.example.librarymanagement.service;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {
    private AdminRepository adminRepository;
}
