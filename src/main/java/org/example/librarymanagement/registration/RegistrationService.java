package org.example.librarymanagement.registration;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.appuser.AppUser;
import org.example.librarymanagement.appuser.AppUserService;
import org.example.librarymanagement.appuser.Role;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    public AppUser register(RegistrationRequest request){
        boolean isEmailValid = emailValidator.test(request.getEmail());

        if(!isEmailValid){
            throw new IllegalStateException("Email is not valid");
        }
        return appUserService.signupUser(new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                LocalDateTime.now(),
                Role.USER));
    }
}
