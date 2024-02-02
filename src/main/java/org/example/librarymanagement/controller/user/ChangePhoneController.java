package org.example.librarymanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.ChangePhoneNumberRequest;
import org.example.librarymanagement.dto.request.OtpVerificationRequest;
import org.example.librarymanagement.service.AccountService;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User APIs")
@RestController
@RequestMapping("${user-mapping}/change-phone")
@RequiredArgsConstructor
public class ChangePhoneController {

    private final AppUserService userService;
    private final AccountService accountService;

    @Operation(summary = "Request Change Phone Number",
            description = "Request to change user phone number",
            tags = {"Phone", "post"})
    @PostMapping
    public ResponseEntity<Void> requestChangePhoneNumber(@Valid @RequestBody ChangePhoneNumberRequest request) {
        userService.requestChangePhoneNumber(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "Confirm OTP for Change Phone Number",
            description = "Confirm OTP to change user phone number",
            tags = {"Phone", "put"})
    @PutMapping
    public ResponseEntity<Void> confirmOTPChangePhoneNumber(@Valid @RequestBody OtpVerificationRequest request) {
        accountService.changePhoneNumber(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
