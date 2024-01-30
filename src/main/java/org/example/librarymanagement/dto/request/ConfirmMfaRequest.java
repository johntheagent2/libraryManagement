package org.example.librarymanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmMfaRequest {
    String secretCode;
    String otp;
}
