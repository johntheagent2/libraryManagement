package org.example.librarymanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("${user-mapping}/logout")
@Tag(name = "User", description = "User APIs")
public class LogoutController {
    private final SessionService sessionService;

    @Operation(summary = "Logout",
            description = "Logout user by getting jwt token then use the email to deactivate session",
            tags = { "logout", "put" })
    @PutMapping
    public ResponseEntity<Void> logout(){
        sessionService.deactivateSession();
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
