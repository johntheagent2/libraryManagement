package org.example.librarymanagement.controller.common;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.RegistrationDTO;
import org.example.librarymanagement.service.Imp.RegistrationServiceImp;
import org.example.librarymanagement.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping()
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationDTO request) throws MessagingException, TemplateException, IOException {
        registrationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmToken(@RequestParam("token") String token){
        registrationService.confirmToken(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/confirm-otp")
    public ResponseEntity<Void> confirmOtpToken(@RequestBody String token){
        registrationService.confirmOtpToken(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/resend")
    public ResponseEntity<Void> resendToken(@RequestParam("email") String email){
        registrationService.resendToken(email);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
