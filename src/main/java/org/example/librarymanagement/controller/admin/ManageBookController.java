package org.example.librarymanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.BookCreateRequest;
import org.example.librarymanagement.dto.response.BookResponse;
import org.example.librarymanagement.service.BookService;
import org.example.librarymanagement.service.criteria.BookCriteria;
import org.example.librarymanagement.service.implement.BookQueryServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Admin Book Management", description = "Admin Book Management APIs")
@RequestMapping("${admin-mapping}/books")
@RestController
@AllArgsConstructor
public class ManageBookController {

    private final BookService bookService;
    private final BookQueryServiceImpl bookQueryService;

    @Operation(summary = "Get All Books",
            description = "Get all books",
            tags = { "Book Management", "get" })
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBook(){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(bookService.getAllBooks());
    }

    @Operation(summary = "Search Books",
            description = "Search books based on criteria",
            tags = { "Book Management", "get" })
    @GetMapping("/search")
    public ResponseEntity<Page<BookResponse>> getQueriedBooks(BookCriteria request, Pageable page){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(bookQueryService.findByCriteria(request, page));
    }

    @Operation(summary = "Add Book with CSV",
            description = "Add multiple books from CSV file",
            tags = { "Book Management", "post" })
    @PostMapping(value = "/csv", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> addBookWithCSV(@RequestParam MultipartFile csv){
        bookService.addListOfBooks(csv);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "Add Book",
            description = "Add a single book",
            tags = { "Book Management", "post" })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> addBook(@ModelAttribute BookCreateRequest request){
        bookService.addBook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "Edit Book",
            description = "Edit a book",
            tags = { "Book Management", "put" })
    @PutMapping( value = "/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> editBook(@ModelAttribute BookCreateRequest request, @PathVariable Long id){
        bookService.editBook(id, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @Operation(summary = "Delete Book",
            description = "Delete a book",
            tags = { "Book Management", "delete" })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
