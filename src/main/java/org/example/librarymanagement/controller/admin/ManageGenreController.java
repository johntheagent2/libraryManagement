package org.example.librarymanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.CreateGenreRequest;
import org.example.librarymanagement.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Genre Management", description = "Admin Genre Management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("${admin-mapping}/genre")
public class ManageGenreController {

    private final GenreService genreService;

    @Operation(summary = "Add Genre",
            description = "Create a new genre with the provided information",
            tags = {"Genre Management", "post"})
    @PostMapping
    public ResponseEntity<Void> addGenre(
            @RequestBody CreateGenreRequest request) {
        genreService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
