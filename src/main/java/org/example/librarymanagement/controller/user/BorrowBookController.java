package org.example.librarymanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.BorrowBookRequest;
import org.example.librarymanagement.dto.request.ReturnBorrowedRequest;
import org.example.librarymanagement.dto.response.BookResponse;
import org.example.librarymanagement.dto.response.BorrowedBookResponse;
import org.example.librarymanagement.service.BorrowReceiptService;
import org.example.librarymanagement.service.implement.BookQueryServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Borrow Book Management", description = "User Borrow Book Management APIs")
@RequestMapping("${user-mapping}/books")
@RestController
@RequiredArgsConstructor
public class BorrowBookController {

    private final BookQueryServiceImpl bookQueryService;
    private final BorrowReceiptService borrowReceiptService;

    @Operation(summary = "Get All Available Books",
            description = "Get All Available Books",
            tags = { "Borrow Book Management", "get" })
    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAvailableBook(Pageable page){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(bookQueryService.getAvailableBook(page));
    }

    @Operation(summary = "Borrow Books",
            description = "Borrow one book per genre",
            tags = { "Borrow Book Management", "post" })
    @PostMapping
    public ResponseEntity<Void> borrowBook(@RequestBody List<BorrowBookRequest> requestList){
        borrowReceiptService.borrow(requestList);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "Get Current Borrowed Books",
            description = "Get Current Borrowed Books",
            tags = { "Get Current Borrowed Books", "get" })
    @GetMapping("/current")
    public ResponseEntity<List<BorrowedBookResponse>> getCurrentBorrowedBook(){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(borrowReceiptService.getCurrentBorrowed());
    }

    @Operation(summary = "Return Current Borrowed Books",
            description = "Return Current Borrowed Books",
            tags = { "Return Current Borrowed Books", "post" })
    @PostMapping("/return")
    public ResponseEntity<Void> returnBorrowedBook(@RequestBody List<ReturnBorrowedRequest> requestList){
        borrowReceiptService.returnBorrowed(requestList);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
