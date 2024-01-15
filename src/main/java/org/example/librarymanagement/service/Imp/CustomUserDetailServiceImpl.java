package org.example.librarymanagement.service.Imp;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.entity.Account;
import org.example.librarymanagement.entity.Admin;
import org.example.librarymanagement.entity.CustomUserDetails;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.repository.AdminRepository;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.service.AdminService;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.ResourceBundle;

@Service
@AllArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService {

    private final AppUserService appUserService;
    private final AdminService adminService;
    private final ResourceBundle resourceBundle;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        return adminService.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseGet(() -> appUserService.findByEmail(email)
                        .map(CustomUserDetails::new)
                        .orElseThrow(() -> new NotFoundException("user.email.email-not-found",
                                resourceBundle.getString("user.email.email-not-found"))));
    }
}