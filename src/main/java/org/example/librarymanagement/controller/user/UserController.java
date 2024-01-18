package org.example.librarymanagement.controller.user;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
