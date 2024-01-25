package org.example.librarymanagement.controller.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.AdminCreateUserRequest;
import org.example.librarymanagement.dto.request.EditUserInfoRequest;
import org.example.librarymanagement.dto.request.UserCriteriaRequest;
import org.example.librarymanagement.dto.response.UserResponse;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.service.AppUserService;
import org.example.librarymanagement.service.criteria.UserCriteria;
import org.example.librarymanagement.service.implement.UserQueryServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("${admin-mapping}/users")
@RestController
@AllArgsConstructor
public class ManageUserController {

    private final AppUserService appUserService;
    private final UserQueryServiceImpl userQueryService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(appUserService.getAllUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponse>> getUsersWithCriteria(UserCriteria request, Pageable page){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userQueryService.findByCriteria(request, page));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addUser(@Valid @RequestBody AdminCreateUserRequest request){
        appUserService.createUser(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestParam Long id){
        appUserService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<EditUserInfoRequest> editUser(@PathVariable Long id, @Valid @RequestBody EditUserInfoRequest request){
        appUserService.editUser(request, id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(request);
    }
}
