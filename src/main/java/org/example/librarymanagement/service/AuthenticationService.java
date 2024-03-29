package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authRequest);

    boolean checkMfaIfAppUser(String email);

    void resetWrongLoginCounter(String email);

    void updateCountWrongLogin(String email);

    AuthenticationResponse refreshToken(String authorization);
}
