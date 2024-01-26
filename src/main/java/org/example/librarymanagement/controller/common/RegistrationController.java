package org.example.librarymanagement.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.OtpVerificationRequest;
import org.example.librarymanagement.dto.request.RegistrationRequest;
import org.example.librarymanagement.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Registration", description = "User Registration APIs")
@RestController
@RequestMapping("${common-mapping}/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @Operation(summary = "Register User",
            description = "Register a new user",
            tags = { "Registration", "post" })
    @PostMapping()
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest request) {
        registrationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Confirm Token",
            description = "Confirm registration token",
            tags = { "Registration", "put" })
    @PutMapping("/confirm")
    public ResponseEntity<Void> confirmToken(
            @Parameter(description = "Registration Token",
                    in = ParameterIn.QUERY, required = true, example = "token123")
            @RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Confirm OTP Token",
            description = "Confirm OTP registration token",
            tags = { "Registration", "put" })
    @PutMapping("/confirm-otp")
    public ResponseEntity<Void> confirmOtpToken(
            @RequestBody OtpVerificationRequest token) {
        registrationService.confirmOtpToken(token.getOtp());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Resend Token",
            description = "Resend registration token",
            tags = { "Registration", "put" })
    @PutMapping("/resend")
    public ResponseEntity<Void> resendToken(
            @Parameter(description = "User Email",
                    in = ParameterIn.QUERY, required = true, example = "user@example.com")
            @RequestParam("email") String email) {
        registrationService.resendToken(email);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
