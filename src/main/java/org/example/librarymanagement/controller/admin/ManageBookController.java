package org.example.librarymanagement.controller.admin;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.BookCreateRequest;
import org.example.librarymanagement.entity.Book;
import org.example.librarymanagement.repository.BookRepository;
import org.example.librarymanagement.service.implement.BookServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("${admin-mapping}/books")
@RestController
@AllArgsConstructor
public class ManageBookController {

    private final BookRepository bookService;

    @GetMapping
    private ResponseEntity<String> getAllBook(){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("hi");
    }

    @PostMapping("/add")
    private ResponseEntity<Void> addABook(@RequestBody BookCreateRequest request){
        Book book = new Book(
                request.getTitle(),
                request.getDescription(),
                request.getQuantity()
        );
        bookService.save(book);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
