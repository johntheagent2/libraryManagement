package org.example.librarymanagement.service.Imp;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminServiceImpl {
    private AdminRepository adminRepository;
}
