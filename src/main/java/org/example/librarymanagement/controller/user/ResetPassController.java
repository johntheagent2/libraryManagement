package org.example.librarymanagement.controller.user;


import lombok.AllArgsConstructor;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("${user-mapping}/reset-password")
public class ResetPassController {

    private final AppUserService appUserService;

    @PutMapping
    public void resetPassword(){
//        appUserService.resetPassword()
    }
}
