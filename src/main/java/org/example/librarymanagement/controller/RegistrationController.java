package org.example.librarymanagement.controller;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.UserDTO;
import org.example.librarymanagement.dto.RegistrationRequestDTO;
import org.example.librarymanagement.exception.ApiRequestException;
import org.example.librarymanagement.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping()
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegistrationRequestDTO request) throws MessagingException, TemplateException, IOException {
        UserDTO appUser = registrationService.register(request);
        return ResponseEntity.ok(appUser);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token){
        return ResponseEntity.ok(registrationService.confirmToken(token));
    }

    @GetMapping("/confirm-otp")
    public ResponseEntity<String> confirmOtpToken(@RequestBody String token){
        return ResponseEntity.ok(registrationService.confirmOtpToken(token));
    }

    @PutMapping("/resend")
    public ResponseEntity<String> resendToken(@RequestParam("email") String email) throws MessagingException, TemplateException, IOException {
        return ResponseEntity.ok(registrationService.resendToken(email));
    }

    @GetMapping
    public ResponseEntity<String> testException(){
        throw new ApiRequestException("Error Test");
    }
}
