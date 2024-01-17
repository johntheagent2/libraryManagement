package org.example.librarymanagement.config.security;


import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.service.CustomUserDetailFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final CustomUserDetailFacade appUserService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> appUserService.loadUserByUsername(username);
    }
}
