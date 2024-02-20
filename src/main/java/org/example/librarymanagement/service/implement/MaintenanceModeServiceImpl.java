package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.common.email.EmailSenderService;
import org.example.librarymanagement.entity.Config;
import org.example.librarymanagement.enumeration.ConfigType;
import org.example.librarymanagement.service.AccountService;
import org.example.librarymanagement.service.ConfigService;
import org.example.librarymanagement.service.MaintenanceModeService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class MaintenanceModeServiceImpl implements MaintenanceModeService {
    private RedisTemplate<String, String> redisTemplate;
    private final AccountService accountService;
    private final EmailSenderService emailSenderService;

    private final String MAINTENANCE_MODE_KEY = "maintenance_mode";
    private final ConfigService configService;

    @Override
    public boolean isEnabled() {
        String value = redisTemplate.opsForValue().get(MAINTENANCE_MODE_KEY);
        return Boolean.parseBoolean(value);
    }

    @Override
    @Transactional
    public void setMode(boolean enabled) {
        List<String> mailList;
        Config redisSettings = Config.builder()
                .key(MAINTENANCE_MODE_KEY)
                .type(ConfigType.BOOLEAN)
                .value(String.valueOf(enabled))
                .build();

        if (enabled) {
            mailList = accountService.getAllEmail();
            emailSenderService.sendMaintenanceMailToAllUser(mailList);
        }

        configService.save(redisSettings);
        redisTemplate.opsForValue()
                .set(redisSettings.getKey(), redisSettings.getValue());
    }
}
