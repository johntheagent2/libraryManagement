package org.example.librarymanagement.controller.user;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.OtpVerificationRequest;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("${default-mapping}/user")
public class UserController {

    private final AppUserService userService;

    @PutMapping("/{email}/enable-mfa")
    public ResponseEntity<MfaResponse> enableMfa(@PathVariable String email){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userService.enableUserMfa(email));
    }
}
