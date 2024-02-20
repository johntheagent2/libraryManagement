package org.example.librarymanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.service.MaintenanceModeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Config", description = "Admin Config APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("${admin-mapping}/maintenance")
public class MaintenanceController {

    private final MaintenanceModeService maintenanceModeService;

    @Operation(summary = "Enable maintenance",
            description = "Turn on maintenance mode",
            tags = {"Admin Config", "post"})
    @PostMapping("/enable")
    public ResponseEntity<Void> enableMaintenance() {
        maintenanceModeService.setMode(true);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Disable maintenance",
            description = "Turn off maintenance mode",
            tags = {"Admin Config", "post"})
    @PostMapping("/disable")
    public ResponseEntity<Void> disableMaintenance() {
        maintenanceModeService.setMode(false);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
