package org.example.librarymanagement.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.PropertySource;

@TestConfiguration
@PropertySource("classpath:/application-test.properties")
public class TestConfig {
}
