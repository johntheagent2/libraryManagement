package org.example.librarymanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCriteriaRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phoneNumber;
}
