package org.example.librarymanagement.controller.common;


import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.ResetPasswordRequest;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("${common-mapping}/reset-password")
public class ResetPassController {

    private final AppUserService appUserService;

    @PutMapping
    public void resetPassword(@RequestBody ResetPasswordRequest request){
        appUserService.resetPassword(request);
    }
}
