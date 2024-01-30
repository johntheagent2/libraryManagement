package org.example.librarymanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MfaResponse {
    private final String secretCode;
    private final String qr;
}
