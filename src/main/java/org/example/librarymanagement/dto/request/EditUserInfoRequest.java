package org.example.librarymanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.example.librarymanagement.common.validator.phonenumber.ValidatePhoneNumber;
import org.example.librarymanagement.common.validator.status.ValidateAccountStatus;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

@Data
@Builder
public class EditUserInfoRequest {
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

    @ValidatePhoneNumber
    private String phoneNumber;

    @Length(min = 8, max = 16, message = "Password should be from 8 to 16 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "At least one uppercase letter, one lowercase letter, one number and one special character")
    private String password;

    @ValidateAccountStatus(message = "Invalid status")
    private String status;

    @Max(value = 3, message = "Should not be greater than 3")
    @Min(value = 0, message = "Smallest value allowed is 0")
    private int countWrongLogin;
}
