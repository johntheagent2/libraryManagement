package org.example.librarymanagement.service.Imp;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.config.security.jwtConfig.JwtService;
import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.response.AuthenticationResponse;
import org.example.librarymanagement.entity.Account;
import org.example.librarymanagement.entity.Admin;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.CustomUserDetails;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final ResourceBundle resourceBundle;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailServiceImpl customUserDetailService;
    private final AppUserServiceImpl appUserService;
    private final AdminServiceImpl adminService;
    private final SessionServiceImpl sessionService;

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        CustomUserDetails account;
        String jwtToken;
        String refreshToken;
        String jti;

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()));

            account = (CustomUserDetails) customUserDetailService.loadUserByUsername(authRequest.getEmail());

            jti = jwtService.generateJti();
            jwtToken = jwtService.generateToken(account, jti);
            refreshToken = jwtService.generateRefreshToken(account, jti);
            sessionService.saveSession(refreshToken);
        }catch (BadCredentialsException e){
            updateCountWrongLogin(authRequest.getEmail());
            throw new NotFoundException("user.account.password-incorrect",
                    resourceBundle.getString("user.account.password-incorrect"));
        }

        resetWrongLoginCounter(authRequest.getEmail());
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void resetWrongLoginCounter(String email) {
        CustomUserDetails account = (CustomUserDetails) customUserDetailService.loadUserByUsername(email);
        Account currentAccount = account.getAccount();

        if (currentAccount instanceof AppUser) {
            appUserService.resetWrongLoginCounter(email);
        } else if (currentAccount instanceof Admin) {
//            TODO: add update admin instead of using saveAdmin()
        }
    }

    public void updateCountWrongLogin(String email){
        CustomUserDetails account = (CustomUserDetails) customUserDetailService.loadUserByUsername(email);
        Account currentAccount = account.getAccount();

        if(currentAccount instanceof AppUser appUser){
            appUser.wrongLoginCount();
            appUserService.updateUser(appUser);
        }else if(currentAccount instanceof Admin admin){
//            TODO: add update admin instead of using saveAdmin()
        }
    }
}
