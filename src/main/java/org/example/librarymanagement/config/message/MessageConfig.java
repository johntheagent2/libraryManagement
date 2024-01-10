package org.example.librarymanagement.config.message;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
public class MessageConfig {
    @Bean
    public ResourceBundle resourceBundle() {
        Locale localeEn = new Locale("vn");
        ResourceBundle labelsUS = ResourceBundle.getBundle("lang/label", localeEn);
        return labelsUS;
    }
}
