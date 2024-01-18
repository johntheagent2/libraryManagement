package org.example.librarymanagement.controller.common;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.AdminCreationRequest;
import org.example.librarymanagement.entity.Admin;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.enumeration.Role;
import org.example.librarymanagement.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${default-mapping}/admin-registration")
@RequiredArgsConstructor
public class RegisterAdmin {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<Admin> registerAdmin(@RequestBody AdminCreationRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.saveAdmin(new Admin(
                        request.getEmail(),
                        request.getPhoneNumber(),
                        request.getPassword(),
                        Role.ROLE_ADMIN,
                        AccountStatus.ACTIVE,
                        LocalDateTime.now()
                )));
    }
}
