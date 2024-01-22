package org.example.librarymanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.example.librarymanagement.common.validator.phonenumber.ValidatePhoneNumber;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@Getter
public class AdminCreationRequest {
    @Email(message = "Invalid email address")
    private String email;

    @ValidatePhoneNumber
    private String phoneNumber;

    @Length(min = 8, max = 16, message = "Password should be from 8 to 16 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "At least one uppercase letter, one lowercase letter, one number and one special character")
    private String password;
}
