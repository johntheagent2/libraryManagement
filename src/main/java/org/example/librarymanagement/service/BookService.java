package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.BookCreateRequest;
import org.example.librarymanagement.dto.response.BookResponse;
import org.example.librarymanagement.entity.Book;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    List<BookResponse> getAllBooks();

    void addBook(BookCreateRequest request);

    void addListOfBooks(MultipartFile file);

    void editBook(Long id, BookCreateRequest request);

    Book findById(Long id);

    void deleteBook(Long id);

    void saveBook(Book book);
}
