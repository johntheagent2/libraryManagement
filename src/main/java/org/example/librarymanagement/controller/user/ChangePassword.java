package org.example.librarymanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.ChangePasswordRequest;
import org.example.librarymanagement.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User APIs")
@RestController
@RequestMapping("${user-mapping}/change-password")
@RequiredArgsConstructor
public class ChangePassword {

    private final AccountService accountService;

    @Operation(summary = "Change Password",
            description = "Change user password",
            tags = {"Password", "put"})
    @PutMapping
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        accountService.changePassword(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
