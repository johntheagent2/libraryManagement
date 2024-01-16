package org.example.librarymanagement.controller.common;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.OtpVerificationRequest;
import org.example.librarymanagement.dto.request.RegistrationRequest;
import org.example.librarymanagement.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${default-mapping}/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping()
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest request) throws MessagingException, TemplateException, IOException {
        registrationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/confirm")
    public ResponseEntity<Void> confirmToken(@RequestParam("token") String token){
        registrationService.confirmToken(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/confirm-otp")
    public ResponseEntity<Void> confirmOtpToken(@RequestBody OtpVerificationRequest token){
        registrationService.confirmOtpToken(token.getOtp());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/resend")
    public ResponseEntity<Void> resendToken(@RequestParam("email") String email){
        registrationService.resendToken(email);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
