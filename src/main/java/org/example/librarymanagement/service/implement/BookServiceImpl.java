package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.BookCreateRequest;
import org.example.librarymanagement.entity.Book;
import org.example.librarymanagement.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl {

    private final BookRepository bookRepository;

    public List<Book> getAllBook(){
        return bookRepository.findAll();
    }

    public void addBook(BookCreateRequest request){
        Book book = new Book(
                request.getTitle(),
                request.getDescription(),
                request.getQuantity()
        );
        saveBook(book);
    }

    public void saveBook(Book book){
        bookRepository.save(book);
    }
}
