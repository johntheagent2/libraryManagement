package org.example.librarymanagement.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
public class AuthenticationResponse {
    private final String jwtToken;
}
