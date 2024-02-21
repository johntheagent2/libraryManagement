package org.example.librarymanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.response.AuthenticationResponse;
import org.example.librarymanagement.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Authentication", description = "Admin Authentication APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("${admin-mapping}/auth")
public class AdminAuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Authenticate Admin",
            description = "Authenticate admin and return JWT token and Refresh token",
            tags = {"Admin Authentication", "post"})
    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.authenticate(authRequest));
    }

    @Operation(summary = "Refresh Token",
            description = "Refresh JWT token using refresh token",
            tags = {"Admin Authentication", "get"})
    @GetMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestHeader("Authorization") String authorization) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authenticationService.refreshToken(authorization));
    }
}
