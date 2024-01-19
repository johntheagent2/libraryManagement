package org.example.librarymanagement.controller.common;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.ResetPasswordRequest;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("${common-mapping}/reset-password")
public class CommonUserInfoChangeController {

    private final AppUserService appUserService;

    @PutMapping
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        appUserService.requestResetPassword(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @PutMapping("/confirm")
    public ResponseEntity<Void> confirmToken(@RequestParam("token") String token){
        appUserService.changeEmail(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/confirm-token")
    public ResponseEntity<Void> confirmResetPassword(@RequestParam String token){
        appUserService.resetPassword(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
