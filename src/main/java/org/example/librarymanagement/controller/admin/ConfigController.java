package org.example.librarymanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.response.ConfigResponse;
import org.example.librarymanagement.service.implement.ConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Admin Config", description = "Admin Config APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("${admin-mapping}/config")
public class ConfigController {
    private final ConfigService configService;

    @Operation(summary = "Get all config settings",
            description = "Get all config settings",
            tags = {"Admin Config", "get"})
    @GetMapping
    public ResponseEntity<List<ConfigResponse>> getALlConfig() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(configService.getAllSettings());
    }
}
