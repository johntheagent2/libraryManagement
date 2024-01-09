package org.example.librarymanagement.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.librarymanagement.model.account.Role;
import org.example.librarymanagement.common.validator.phoneNumber.ValidPhoneNumber;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
public class RegistrationRequestDTO {

    @NotNull(message = "First Name should not be empty!")
    private String firstName;

    @NotNull(message = "First Name should not be empty!")
    private String lastName;

    @NotNull(message = "Address should not be empty!")
    private String address;

    @Email(message = "Invalid email address")
    private String email;

    @ValidPhoneNumber
    private String phoneNumber;

    @Length(min = 8, max = 16, message = "Password should be from 8 to 16 characters")
    private String password;

    private Role role;

    private LocalDateTime creationDate;
}
