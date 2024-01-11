package org.example.librarymanagement.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phoneNumber;
}
