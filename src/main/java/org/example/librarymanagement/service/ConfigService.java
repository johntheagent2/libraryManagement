package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.response.ConfigResponse;
import org.example.librarymanagement.entity.Config;

import java.util.List;

public interface ConfigService {
    void save(Config redisSettings);

    List<ConfigResponse> getAllSettings();

    Config getByKey(String key);
}
