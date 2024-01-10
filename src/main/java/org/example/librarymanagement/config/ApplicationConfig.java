package org.example.librarymanagement.config;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.exception.serviceException.ServiceException;
import org.example.librarymanagement.repository.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ResourceBundle;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AppUserRepository appUserRepository;
    private final ResourceBundle resourceBundle;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> appUserRepository.findByEmail(username)
                .orElseThrow(() -> new ServiceException("user.email.email-not-found",
                        resourceBundle.getString("user.email.email-not-found")));
    }

}
