package org.example.librarymanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.librarymanagement.common.validator.phonenumber.ValidatePhoneNumber;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePhoneNumberRequest {

    @ValidatePhoneNumber
    private String phoneNumber;
}
