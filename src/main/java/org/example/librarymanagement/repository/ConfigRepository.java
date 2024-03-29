package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigRepository extends JpaRepository<Config, Long> {
    Optional<Config> findByKey(String key);

    Optional<Config> getConfigByKey(String key);
}
