package org.example.librarymanagement.controller.common;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("${default-mapping}/logout")
public class LogoutController {
    private final SessionService sessionService;

    @PutMapping
    public ResponseEntity<Void> logout(){
        sessionService.deactivateSession();
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
