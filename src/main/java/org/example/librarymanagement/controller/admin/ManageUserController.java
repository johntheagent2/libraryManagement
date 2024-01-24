package org.example.librarymanagement.controller.admin;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.UserCriteriaRequest;
import org.example.librarymanagement.dto.response.UserResponse;
import org.example.librarymanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("${admin-mapping}/users")
@RestController
@AllArgsConstructor
public class ManageUserController {

    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(appUserService.getAllUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> getUsersWithCriteria(@RequestBody UserCriteriaRequest request){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(appUserService.getUsersWithCriteria(request));
    }
}
