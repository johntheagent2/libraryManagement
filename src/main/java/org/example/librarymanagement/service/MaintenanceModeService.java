package org.example.librarymanagement.service;

import org.springframework.transaction.annotation.Transactional;

public interface MaintenanceModeService {
    boolean isEnabled();

    @Transactional
    void setMode(boolean enabled);
}
