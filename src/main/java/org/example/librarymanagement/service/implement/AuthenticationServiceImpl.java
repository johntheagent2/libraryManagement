package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.config.security.jwtConfig.JwtService;
import org.example.librarymanagement.dto.request.AuthenticationRequest;
import org.example.librarymanagement.dto.response.AuthenticationResponse;
import org.example.librarymanagement.entity.*;
import org.example.librarymanagement.entity.base.Account;
import org.example.librarymanagement.exception.exception.BadCredentialException;
import org.example.librarymanagement.service.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final ResourceBundle resourceBundle;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailFacade customUserDetailService;
    private final AppUserService appUserService;
    private final SessionService sessionService;
    private final GoogleAuthenticatorService googleAuthenticatorService;

    @Override
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

            if(checkMfaIfAppUser(authRequest.getEmail())){
                googleAuthenticatorService.validateTOTP((AppUser) account.getAccount(), authRequest.getOtp());
            }

            jti = jwtService.generateJti();
            jwtToken = jwtService.generateToken(account, jti);
            refreshToken = jwtService.generateRefreshToken(account, jti);
            sessionService.saveSession(refreshToken);
        }catch (BadCredentialsException e){
            updateCountWrongLogin(authRequest.getEmail());
            throw new BadCredentialException("user.account.password-incorrect",
                    resourceBundle.getString("user.account.password-incorrect"));
        }

        resetWrongLoginCounter(authRequest.getEmail());
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public boolean checkMfaIfAppUser(String email){
        CustomUserDetails account = (CustomUserDetails) customUserDetailService.loadUserByUsername(email);
        Account currentAccount = account.getAccount();

        if (currentAccount instanceof AppUser) {
            return appUserService.isUserEnableMfa(email);
        }
        return false;
    }

    @Override
    public void resetWrongLoginCounter(String email) {
        CustomUserDetails account = (CustomUserDetails) customUserDetailService.loadUserByUsername(email);
        Account currentAccount = account.getAccount();

        if (currentAccount instanceof AppUser) {
            appUserService.resetWrongLoginCounter(email);
        } else if (currentAccount instanceof Admin) {
//            TODO: add update admin instead of using saveAdmin()
        }
    }

    @Override
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

    @Override
    public AuthenticationResponse refreshToken(String authorization){
        String oldRefreshToken;
        String newRefreshToken;
        String newJwtToken;
        UserDetails userDetails;

        userDetails = getUserDetails();
        oldRefreshToken = jwtService.extractJwtToken(authorization);
        newJwtToken = jwtService.generateToken(userDetails, jwtService.extractJti(oldRefreshToken));
        newRefreshToken = jwtService.generateRefreshToken(userDetails,
                jwtService.extractJti(oldRefreshToken),
                jwtService.extractIssueAt(oldRefreshToken),
                jwtService.extractExpiration(oldRefreshToken));


        JwtService.validateRefreshToken(authorization, jwtService, sessionService, resourceBundle);

        return new AuthenticationResponse(newJwtToken, newRefreshToken);
    }

    private UserDetails getUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) authentication.getPrincipal();
    }


}
