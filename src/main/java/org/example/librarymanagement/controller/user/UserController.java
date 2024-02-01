package org.example.librarymanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.*;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.service.AccountService;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User APIs")
@RestController
@RequestMapping("${user-mapping}")
@RequiredArgsConstructor
public class UserController {

    private final AppUserService userService;
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<Void> test() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Enable MFA",
            description = "Send QR code and secret key back to user",
            tags = { "Mfa", "get" })
    @GetMapping("/mfa")
    public ResponseEntity<MfaResponse> enableMfa() {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userService.enableUserMfa());
    }

    @Operation(summary = "Confirm MFA",
            description = "Confirm OTP and secret key, then update user MFA status",
            tags = { "Mfa", "put" })
    @PutMapping("/mfa")
    public ResponseEntity<Void> confirmMfa(@RequestBody ConfirmMfaRequest request) {
        userService.confirmMFA(request.getSecretCode(), request.getOtp());
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @Operation(summary = "Change Password",
            description = "Change user password",
            tags = { "Password", "put" })
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        accountService.changePassword(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @Operation(summary = "Request Change Phone Number",
            description = "Request to change user phone number",
            tags = { "Phone", "post" })
    @PostMapping("/change-phone")
    public ResponseEntity<Void> requestChangePhoneNumber(@Valid @RequestBody ChangePhoneNumberRequest request) {
        userService.requestChangePhoneNumber(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "Confirm OTP for Change Phone Number",
            description = "Confirm OTP to change user phone number",
            tags = { "Phone", "put" })
    @PutMapping("change-phone/confirm")
    public ResponseEntity<Void> confirmOTPChangePhoneNumber(@Valid @RequestBody OtpVerificationRequest request) {
        accountService.changePhoneNumber(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @Operation(summary = "Request Change Email",
            description = "Request to change user email",
            tags = { "Email", "post" })
    @PostMapping("/change-mail")
    public ResponseEntity<Void> requestChangeMail(@Valid @RequestBody ChangeEmailRequest request) {
        userService.requestChangeEmail(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @Operation(summary = "Confirm Change Email",
            description = "Confirm change user email using the provided token",
            tags = { "Email", "put" })
    @PutMapping("/change-mail/confirm")
    public ResponseEntity<Void> changeMail(@RequestParam String token) {
        accountService.changeEmail(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
