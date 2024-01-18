package org.example.librarymanagement.config;


import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.config.audit.AuditorAwareImpl;
import org.example.librarymanagement.service.CustomUserDetailFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
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
