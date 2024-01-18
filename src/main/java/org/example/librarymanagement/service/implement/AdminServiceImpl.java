package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.entity.Admin;
import org.example.librarymanagement.repository.AdminRepository;
import org.example.librarymanagement.service.AdminService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{
    private AdminRepository adminRepository;
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public Admin saveAdmin(Admin admin) {
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        adminRepository.save(admin);
        return admin;
    }

    @Override
    public void updateUser(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
