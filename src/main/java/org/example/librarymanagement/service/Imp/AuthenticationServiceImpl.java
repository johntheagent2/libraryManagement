package org.example.librarymanagement.service.Imp;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.config.security.jwtConfig.JwtService;
import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.response.AuthenticationResponse;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl {

    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ResourceBundle resourceBundle;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailServiceImpl customUserDetailService;

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        UserDetails account;
        String jwtToken;

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new NotFoundException("user.account.password-incorrect",
                    resourceBundle.getString("user.account.password-incorrect"));
        }

        account = customUserDetailService.loadUserByUsername(authRequest.getEmail());
        jwtToken = jwtService.generateToken(account);

        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }

    public boolean validatePassword(String requestPassword, String userDetailsPassword){
        return passwordEncoder.matches(requestPassword, userDetailsPassword);
    }
}
