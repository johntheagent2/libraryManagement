package org.example.librarymanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.CreateAuthorRequest;
import org.example.librarymanagement.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Admin Author Management", description = "Admin Author Management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("${admin-mapping}/author")
public class ManageAuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Add Author",
            description = "Create a new author with the provided information",
            tags = {"Author Management", "post"})
    @PostMapping
    public ResponseEntity<Void> addAuthor(@RequestBody CreateAuthorRequest request) {
        authorService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
