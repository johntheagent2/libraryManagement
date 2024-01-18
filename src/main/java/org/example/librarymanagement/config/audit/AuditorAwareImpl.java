package org.example.librarymanagement.config.audit;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Implement logic to retrieve the current username or identifier
        return Optional.of("username");
    }
}
