package org.example.librarymanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.librarymanagement.common.validator.phonenumber.ValidPhoneNumber;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@Getter
public class RegistrationRequest {

    @NotNull(message = "First Name should not be empty!")
    @Length(max = 50, message = "First name should be max 50 characters")
    private String firstName;

    @NotNull(message = "Last Name should not be empty!")
    @Length(max = 50, message = "Last name should be max 50 characters")
    private String lastName;

    @NotNull(message = "Address should not be empty!")
    @Length(max = 50, message = "Address is too long")
    private String address;

    @Email(message = "Invalid email address")
    private String email;

    @ValidPhoneNumber
    private String phoneNumber;

    @Length(min = 8, max = 16, message = "Password should be from 8 to 16 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
    message = "At least one uppercase letter, one lowercase letter, one number and one special character")
    private String password;
}
