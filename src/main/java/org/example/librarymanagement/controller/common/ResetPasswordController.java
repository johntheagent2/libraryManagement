package org.example.librarymanagement.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.ResetPasswordRequest;
import org.example.librarymanagement.service.AccountService;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reset Password", description = "Reset Password APIs")
@AllArgsConstructor
@RestController
@RequestMapping("${common-mapping}/reset-password")
public class ResetPasswordController {

    private final AppUserService appUserService;
    private final AccountService accountService;

    @Operation(summary = "Request Reset Password",
            description = "Request to reset user password",
            tags = {"Reset Password", "post"})
    @PostMapping
    public ResponseEntity<Void> requestResetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        appUserService.requestResetPassword(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @Operation(summary = "Confirm Reset Password",
            description = "Confirm reset password token",
            tags = {"Reset Password", "put"})
    @PutMapping
    public ResponseEntity<Void> confirmResetPassword(
            @Parameter(description = "Reset Password Token",
                    in = ParameterIn.QUERY, required = true, example = "token123")
            @RequestParam String token,
            @Parameter(description = "User Email",
                    in = ParameterIn.QUERY, required = true, example = "user@example.com")
            @RequestParam String email) {
        accountService.resetPassword(token, email);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
