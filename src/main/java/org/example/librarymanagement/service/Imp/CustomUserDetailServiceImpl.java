package org.example.librarymanagement.service.Imp;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.entity.Account;
import org.example.librarymanagement.entity.Admin;
import org.example.librarymanagement.entity.CustomUserDetails;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.exception.exception.UnauthorizedException;
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

        CustomUserDetails userDetails = adminService.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseGet(() -> appUserService.findByEmail(email)
                        .map(CustomUserDetails::new)
                        .orElseThrow(() -> new NotFoundException("user.email.email-not-found",
                                resourceBundle.getString("user.email.email-not-found"))));

        if(userDetails.isEnabled()){
            if(userDetails.getAccount().getCountWrongLogin() < 3){
                return userDetails;
            }else {
                throw new UnauthorizedException("user.account.login-exceeded",
                        resourceBundle.getString("user.account.login-exceeded"));
            }
        }else{
            throw new NotFoundException("user.account.account-not-enabled",
                    resourceBundle.getString("user.account.account-not-enabled"));
        }
        
    }
}