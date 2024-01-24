package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.entity.Admin;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.base.Account;
import org.example.librarymanagement.entity.CustomUserDetails;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.exception.exception.UnauthorizedException;
import org.example.librarymanagement.service.AdminService;
import org.example.librarymanagement.service.AppUserService;
import org.example.librarymanagement.service.CustomUserDetailFacade;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@Service
@AllArgsConstructor
public class CustomUserDetailServiceImpl implements CustomUserDetailFacade {

    private final AppUserService appUserService;
    private final AdminService adminService;
    private final ResourceBundle resourceBundle;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomUserDetails userDetails = findUserDetailsByEmail(email);
        validateUserDetails(userDetails);
        return userDetails;
    }

    private CustomUserDetails findUserDetailsByEmail(String email) {
        AppUser appUser = appUserService.findByEmail(email).orElse(null);
        if (appUser != null) {
            return new CustomUserDetails(appUser);
        }

        Admin admin = adminService.findByEmail(email).orElse(null);
        if (admin != null) {
            return new CustomUserDetails(admin);
        }

        throw new NotFoundException("user.email.email-not-found",
                resourceBundle.getString("user.email.email-not-found"));
    }

    private void validateUserDetails(CustomUserDetails userDetails) {
        Account account = userDetails.getAccount();

        if (account.getStatus().equals(AccountStatus.ACTIVE)) {
            if (userDetails.getAccount().getCountWrongLogin() >= 3) {
                throw new UnauthorizedException("user.account.login-exceeded",
                        resourceBundle.getString("user.account.login-exceeded"));
            }
        }

        if(account.getStatus().equals(AccountStatus.UNVERIFIED)) {
            throw new NotFoundException("user.account.account-not-enabled",
                    resourceBundle.getString("user.account.account-not-enabled"));
        }

        if (account.getStatus().equals(AccountStatus.BLOCKED)) {
            throw new NotFoundException("user.account.account-blocked",
                    resourceBundle.getString("user.account.account-blocked"));
        }

        if (!userDetails.isEnabled()) {
            throw new NotFoundException("user.account.account-deleted",
                    resourceBundle.getString("user.account.account-deleted"));
        }
    }
}