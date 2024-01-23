package org.example.librarymanagement.controller.common;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.ResetPasswordRequest;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("${common-mapping}/reset-password")
public class ResetPasswordController {

    private final AppUserService appUserService;

    @PutMapping
    public ResponseEntity<Void> requestResetPassword(@Valid @RequestBody ResetPasswordRequest request){
        appUserService.requestResetPassword(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @DeleteMapping("/confirm-token")
    public ResponseEntity<Void> confirmResetPassword(@RequestParam String token, @RequestParam String email){
        appUserService.resetPassword(token, email);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
