package org.example.librarymanagement.appuser;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.registration.token.ConfirmationToken;
import org.example.librarymanagement.registration.token.ConfirmationTokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AppUserService implements UserDetailsService {

    private final String USER_NOT_FOUND_MESSAGE = "User with email %s is not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private  final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));
    }

    @Transactional
    public AppUser signupUser(AppUser appUser){
        boolean isUserExist = appUserRepository.findByEmail(appUser.getEmail())
                .isPresent();

        if(isUserExist){
            throw new IllegalStateException("Email is registered already!");
        }

        createUser(appUser);
        createToken(appUser);

        return appUser;
    }

    private void createUser(AppUser appUser){
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
    }

    private void createToken(AppUser appUser){
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                appUser);

        confirmationTokenService.saveConfirmationToken(confirmationToken);
    }
}
