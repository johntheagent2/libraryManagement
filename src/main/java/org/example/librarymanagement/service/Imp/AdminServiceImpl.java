package org.example.librarymanagement.service.Imp;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.entity.Admin;
import org.example.librarymanagement.repository.AdminRepository;
import org.example.librarymanagement.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{
    private AdminRepository adminRepository;


    @Override
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
