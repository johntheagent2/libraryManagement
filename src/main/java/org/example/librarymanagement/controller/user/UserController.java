package org.example.librarymanagement.controller.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.ChangeEmailRequest;
import org.example.librarymanagement.dto.request.ChangePasswordRequest;
import org.example.librarymanagement.dto.request.ChangePhoneNumberRequest;
import org.example.librarymanagement.dto.request.OtpVerificationRequest;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("${user-mapping}")
public class UserController {

    private final AppUserService userService;

    @PutMapping("/{email}/enable-mfa")
    public ResponseEntity<MfaResponse> enableMfa(@PathVariable String email){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userService.enableUserMfa(email));
    }

    @PutMapping("/{email}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable String email, @Valid @RequestBody ChangePasswordRequest request){
        userService.changePassword(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @PostMapping("/{email}/change-phone")
    public ResponseEntity<Void> requestChangePhoneNumber(@PathVariable String email, @Valid @RequestBody ChangePhoneNumberRequest request){
        userService.requestChangePhoneNumber(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{email}/change-phone/confirm")
    public ResponseEntity<Void> confirmOTPChangePhoneNumber(@PathVariable String email, @Valid @RequestBody OtpVerificationRequest request){
        userService.changePhoneNumber(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @PostMapping("/{email}/change-mail")
    public ResponseEntity<Void> requestChangeMail(@PathVariable String email, @Valid @RequestBody ChangeEmailRequest request){
        userService.requestChangeEmail(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @PostMapping("/{email}/change-mail/confirm")
    public ResponseEntity<Void> changeMail(@PathVariable String email, @RequestParam String token){
        userService.changeEmail(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
