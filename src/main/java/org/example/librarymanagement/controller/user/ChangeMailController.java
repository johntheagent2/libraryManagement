package org.example.librarymanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.ChangeEmailRequest;
import org.example.librarymanagement.service.AccountService;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User APIs")
@RestController
@RequestMapping("${user-mapping}/change-mail")
@RequiredArgsConstructor
public class ChangeMailController {

    private final AppUserService userService;
    private final AccountService accountService;

    @Operation(summary = "Request Change Email",
            description = "Request to change user email",
            tags = {"Email", "post"})
    @PostMapping
    public ResponseEntity<Void> requestChangeMail(@Valid @RequestBody ChangeEmailRequest request) {
        userService.requestChangeEmail(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @Operation(summary = "Confirm Change Email",
            description = "Confirm change user email using the provided token",
            tags = {"Email", "put"})
    @PutMapping
    public ResponseEntity<Void> changeMail(@RequestParam String token) {
        accountService.changeEmail(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
