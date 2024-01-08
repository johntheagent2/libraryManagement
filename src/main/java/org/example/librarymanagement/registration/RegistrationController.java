package org.example.librarymanagement.registration;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping()
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request){
        String appUser = registrationService.register(request);
        return ResponseEntity.ok(appUser);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token){
        return ResponseEntity.ok(registrationService.confirmToken(token));
    }
}
