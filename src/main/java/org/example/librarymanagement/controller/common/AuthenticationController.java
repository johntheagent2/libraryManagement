package org.example.librarymanagement.controller.common;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.response.AuthenticationResponse;
import org.example.librarymanagement.service.Imp.AuthenticationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${default-mapping}/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.authenticate(authRequest));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestHeader String Authorization){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authenticationService.refreshToken(Authorization));
    }
}
