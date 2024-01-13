package org.example.librarymanagement.controller.common;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.response.AuthenticationResponse;
import org.example.librarymanagement.service.Imp.AuthenticationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${default-mapping}/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authRequest){
        System.out.println(authRequest.getEmail() + " " + authRequest.getPassword());
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authRequest);
        System.out.println(authenticationResponse.getJwtToken());
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }
}
