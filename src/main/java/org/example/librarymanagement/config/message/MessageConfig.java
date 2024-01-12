package org.example.librarymanagement.config.message;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
public class MessageConfig {
    @Bean
    public ResourceBundle resourceBundle() {
        Locale localeEn = new Locale("en");
        return ResourceBundle.getBundle("lang/label", localeEn);
    }
}
