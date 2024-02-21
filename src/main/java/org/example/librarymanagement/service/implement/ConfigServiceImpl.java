package org.example.librarymanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.response.ConfigResponse;
import org.example.librarymanagement.entity.Config;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.repository.ConfigRepository;
import org.example.librarymanagement.service.ConfigService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ResourceBundle;

@RequiredArgsConstructor
@Service
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository redisSettingRepository;
    private final ResourceBundle resourceBundle;

    @Override
    public void save(Config redisSettings) {
        redisSettingRepository.findByKey(redisSettings.getKey())
                .ifPresent(settings -> redisSettings.setId(settings.getId()));

        redisSettingRepository.save(redisSettings);
    }

    @Override
    public List<ConfigResponse> getAllSettings() {
        return redisSettingRepository.findAll()
                .stream().map((redisSettings -> ConfigResponse.builder()
                        .key(redisSettings.getKey())
                        .value(redisSettings.getValue())
                        .build())).toList();
    }

    @Override
    public Config getByKey(String key) {
        return redisSettingRepository.findByKey(key)
                .orElseThrow(() -> new BadRequestException("service.redis.not-found",
                        resourceBundle.getString("service.redis.not-found")));
    }
}
