package org.example.librarymanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.AdminCreateUserRequest;
import org.example.librarymanagement.dto.request.EditUserInfoRequest;
import org.example.librarymanagement.dto.response.UserResponse;
import org.example.librarymanagement.service.AppUserService;
import org.example.librarymanagement.service.criteria.UserCriteria;
import org.example.librarymanagement.service.implement.AppUserQueryServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin User Management", description = "Admin User Management APIs")
@RequestMapping("${admin-mapping}/users")
@RestController
@AllArgsConstructor
public class ManageUserController {

    private final AppUserService appUserService;
    private final AppUserQueryServiceImpl userQueryService;

    @Operation(summary = "Test Endpoint",
            description = "Test endpoint to check if the API is working",
            tags = { "User Management", "get" })
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Worked!");
    }

    @Operation(summary = "Get Users with Criteria",
            description = "Get a list of users based on specified criteria",
            tags = { "User Management", "get" })
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsersWithCriteria(
            @Parameter(description = "User search criteria",
                    in = ParameterIn.QUERY)
            UserCriteria request,
            @Parameter(description = "Page information",
                    in = ParameterIn.QUERY)
            Pageable page) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userQueryService.findByCriteria(request, page));
    }

    @Operation(summary = "Add User",
            description = "Create a new user with the provided information",
            tags = { "User Management", "post" })
    @PostMapping
    public ResponseEntity<Void> addUser(
            @Valid @RequestBody AdminCreateUserRequest request) {
        appUserService.createUser(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @Operation(summary = "Delete User",
            description = "Delete a user based on the provided user ID",
            tags = { "User Management", "delete" })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID to delete",
                    in = ParameterIn.PATH)
            @PathVariable Long id) {
        appUserService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @Operation(summary = "Edit User",
            description = "Edit a user based on the provided user ID and updated information",
            tags = { "User Management", "put" })
    @PutMapping("/{id}")
    public ResponseEntity<EditUserInfoRequest> editUser(
            @Parameter(description = "User ID to edit",
                    in = ParameterIn.PATH)
            @PathVariable Long id,
            @Valid @RequestBody EditUserInfoRequest request) {
        appUserService.editUser(request, id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(request);
    }
}
