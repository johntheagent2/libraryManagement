package org.example.librarymanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.ConfirmMfaRequest;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User APIs")
@RestController
@RequestMapping("${user-mapping}")
@RequiredArgsConstructor
public class MfaController {

    private final AppUserService userService;

    @Operation(summary = "Enable MFA",
            description = "Send QR code and secret key back to user",
            tags = {"Mfa", "get"})
    @GetMapping
    public ResponseEntity<MfaResponse> enableMfa() {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userService.enableUserMfa());
    }

    @Operation(summary = "Confirm MFA",
            description = "Confirm OTP and secret key, then update user MFA status",
            tags = {"Mfa", "put"})
    @PutMapping
    public ResponseEntity<Void> confirmMfa(@RequestBody ConfirmMfaRequest request) {
        userService.confirmMFA(request.getSecretCode(), request.getOtp());
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
