package org.example.librarymanagement.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Email(message = "Invalid email address")
    private String email;

    @NotNull
    private String password;

    @Nullable
    private int otp;
}
