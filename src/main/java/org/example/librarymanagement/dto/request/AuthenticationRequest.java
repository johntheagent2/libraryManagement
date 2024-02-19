package org.example.librarymanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @Email(message = "Invalid email address")
    private String email;

    @NotNull
    private String password;

    @Nullable
    private int otp;
}
