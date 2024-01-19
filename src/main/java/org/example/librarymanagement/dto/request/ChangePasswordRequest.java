package org.example.librarymanagement.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.librarymanagement.common.validator.changepassword.changed.ValidateChangedPassword;
import org.example.librarymanagement.common.validator.changepassword.current.ValidateCurrentPassword;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @Length(min = 8, max = 16, message = "Password should be from 8 to 16 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "At least one uppercase letter, one lowercase letter, one number and one special character")
    @ValidateCurrentPassword
    private String currentPassword;

    @Length(min = 8, max = 16, message = "Password should be from 8 to 16 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "At least one uppercase letter, one lowercase letter, one number and one special character")
    @ValidateChangedPassword
    private String newPassword;
}
