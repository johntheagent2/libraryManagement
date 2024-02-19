package org.example.librarymanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.BookCreateRequest;
import org.example.librarymanagement.dto.response.BookResponse;
import org.example.librarymanagement.entity.Book;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.repository.BookRepository;
import org.example.librarymanagement.service.AuthorService;
import org.example.librarymanagement.service.BookService;
import org.example.librarymanagement.service.GenreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final ResourceBundle resourceBundle;

    @Value("${img.default.book-cover}")
    private String defaultBookCover;

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> BookResponse.builder()
                        .id(book.getId())
                        .picture(book.getPicture())
                        .title(book.getTitle())
                        .description(book.getDescription())
                        .quantity(book.getQuantity())
                        .genre(book.getGenre().getName())
                        .author(book.getAuthor().getName())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void addBook(BookCreateRequest request) {
        String fileName;
        Book book;
        AtomicInteger currentQuantity = new AtomicInteger(request.getQuantity());
        AtomicLong id = new AtomicLong();
        try {
            fileName = BookFileProcess.saveUploadedFile(request.getPicture(), resourceBundle);

            book = Book.builder()
                    .picture(fileName)
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .quantity(currentQuantity.get())
                    .price(request.getPrice())
                    .author(authorService.findAuthor(request.getAuthorId()))
                    .genre(genreService.findGenre(request.getGenreId()))
                    .removed(false)
                    .build();

            findByTitle(request.getTitle())
                    .ifPresent(
                            existedBook -> {
                                book.setId(existedBook.getId());
                                book.setQuantity(
                                        book.getQuantity() + existedBook.getQuantity()
                                );
                            });


            saveBook(book);

        } catch (IOException e) {
            throw new org.example.librarymanagement.exception.exception.IOException(
                    resourceBundle.getString("util.io.exception"),
                    "util.io.exception"
            );
        }
    }

    @Override
    @Transactional
    public void addListOfBooks(MultipartFile file) {
        List<BookCreateRequest> bookCreateRequestList = BookFileProcess.addCSV(file, resourceBundle);
        bookCreateRequestList.forEach(this::addBook);
    }


    @Override
    @Transactional
    public void editBook(Long id, BookCreateRequest request) {
        String fileName;
        Book book;
        String currentImg;
        try {
            fileName = BookFileProcess.saveUploadedFile(request.getPicture(), resourceBundle);
            currentImg = findById(id).getPicture();
            if (!currentImg.equals(defaultBookCover)) {
                BookFileProcess.deleteFile(currentImg);
            }
            book = Book.builder()
                    .id(id)
                    .picture(fileName)
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .quantity(request.getQuantity())
                    .author(authorService.findAuthor(request.getAuthorId()))
                    .genre(genreService.findGenre(request.getGenreId()))
                    .removed(request.isRemoved())
                    .build();
            saveBook(book);

        } catch (IOException e) {
            throw new org.example.librarymanagement.exception.exception.IOException(
                    resourceBundle.getString("util.io.exception"),
                    "util.io.exception"
            );
        }
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(resourceBundle.getString("service.book.not-found"),
                        "service.book.not-found"));
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setRemoved(true);
        saveBook(book);
    }

    @Override
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public Optional<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
}
