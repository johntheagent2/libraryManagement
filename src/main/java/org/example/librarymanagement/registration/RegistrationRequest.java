package org.example.librarymanagement.registration;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
public class RegistrationRequest {

    @NotNull(message = "First Name should not be empty!")
    private String firstName;

    @NotNull(message = "First Name should not be empty!")
    private String lastName;

    @NotNull(message = "Address should not be empty!")
    private String address;

    @Email(message = "Invalid email address")
    private String email;

    @Length(min = 10, max = 11, message = "Phone number should be from 10 to 11 characters")
    @Pattern(regexp = "[0-9]+", message = "Invalid phone number")
    private String phoneNumber;

    @Length(min = 8, max = 16, message = "Password should be from 8 to 16 characters")
    private String password;

    private LocalDateTime creationDate;
}
