package org.example.librarymanagement.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.NotFound;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
public class RegistrationRequest {

    @NotNull(message = "First Name should not be empty!")
    private String firstName;

    @NotNull(message = "First Name should not be empty!")
    private String lastName;

    @Email(message = "Invalid email address")
    private String email;

    @Min(value = 8, message = "Should be at least 8 characters")
    private String password;

    private LocalDateTime creationDate;
}
