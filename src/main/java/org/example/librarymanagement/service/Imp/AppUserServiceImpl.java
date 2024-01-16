package org.example.librarymanagement.service.Imp;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.ResourceBundle;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService{

    private final AppUserRepository appUserRepository;
    private final ResourceBundle resourceBundle;
    private final BCryptPasswordEncoder passwordEncoder;

    public void verifyUser(AppUser user){
        user.setStatus(AccountStatus.ACTIVE);
        updateUser(user);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findAppUserByEmail(email);
    }

    @Override
    public void resetWrongLoginCounter(String email) {
        appUserRepository.resetWrongLoginCounter(email);
    }

    public void saveUser(AppUser appUser){
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
    }

    @Override
    public void updateUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }
}
