package org.example.librarymanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.common.Global;
import org.example.librarymanagement.common.email.EmailSenderService;
import org.example.librarymanagement.dto.request.BorrowBookRequest;
import org.example.librarymanagement.dto.request.ReturnBorrowedRequest;
import org.example.librarymanagement.dto.response.BookResponse;
import org.example.librarymanagement.dto.response.BorrowedBookResponse;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.Book;
import org.example.librarymanagement.entity.BorrowReceipt;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.exception.exception.OptimisticLockException;
import org.example.librarymanagement.repository.BorrowReceiptRepository;
import org.example.librarymanagement.service.AppUserService;
import org.example.librarymanagement.service.BookService;
import org.example.librarymanagement.service.BorrowReceiptService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BorrowReceiptServiceImpl implements BorrowReceiptService {

    private final BorrowReceiptRepository borrowReceiptRepository;
    private final BookService bookService;
    private final AppUserService appUserService;
    private final ResourceBundle resourceBundle;
    private final EmailSenderService emailSenderService;

    @Override
    @Transactional(readOnly = true)
    public List<BorrowedBookResponse> getCurrentBorrowed(){
        String email = Global.getCurrentLogin(resourceBundle).getUsername();
        boolean isActive = true;
        List<BorrowedBookResponse> bookResponseList = new ArrayList<>();
        getReceiptByEmailAndActive(email, isActive).ifPresent(
                borrowReceipt -> {
                    bookResponseList.addAll(convertToBookResponse(borrowReceipt.getBookList()));
                }
        );
        return bookResponseList;
    }

    @Override
    @Transactional
    public void borrow(List<BorrowBookRequest> requestList) {
        String email = Global.getCurrentLogin(resourceBundle).getUsername();
        AppUser appUser;
        BorrowReceipt borrowReceipt;
        List<Book> allBooks;

        checkRequestList(requestList);
        checkBookPerGenre(requestList);
        isBookBorrowable(requestList);
        checkForActiveBorrowSession();

        allBooks = toList(requestList);

        try{
            appUser = appUserService.getAppUser(email);
            borrowReceipt = new BorrowReceipt(allBooks, appUser);
            borrowReceiptRepository.save(borrowReceipt);
//        emailSenderService.sendInvoiceEmail(email, allBooks, borrowReceipt.getTotalPrice());
        }catch (OptimisticLockException e){
            throw new OptimisticLockException(resourceBundle.getString("service.borrow-book.conflict"),
                    "service.borrow-book.conflict");
        }

    }

    @Transactional
    public void returnBorrowed(List<ReturnBorrowedRequest> requestList) {
        String email = Global.getCurrentLogin(resourceBundle).getUsername();
        boolean isActive = true;
        List<Book> currentBookList;

        BorrowReceipt borrowReceipt = getReceiptByEmailAndActive(email, isActive)
                .orElseThrow(() -> new NotFoundException(
                        resourceBundle.getString("service.return-book.not-found"),
                        "service.return-book.not-found"));

        currentBookList = borrowReceipt.getBookList();

        try{
            checkList(requestList);
            processReturnedBooks(requestList, currentBookList);
        }catch (OptimisticLockException e){
            throw new OptimisticLockException(resourceBundle.getString("service.return-book.conflict"),
                    "service.return-book.conflict");
        }

        if (currentBookList.isEmpty()) {
            borrowReceiptRepository.updateBorrowSession(borrowReceipt.getId());
        }
    }

    private void processReturnedBooks(List<ReturnBorrowedRequest> requestList, List<Book> currentBookList) {
        requestList.forEach(bookId -> removeAndUpdateBook(bookId.getId(), currentBookList));
    }

    private void removeAndUpdateBook(Long bookId, List<Book> currentBookList) {
        currentBookList.removeIf(currentBook -> bookId.equals(currentBook.getId()));
        updateBookQuantity(currentBookList);
    }

    private void updateBookQuantity(List<Book> currentBookList) {
        currentBookList.forEach(currentBook -> {
            int quantity = currentBook.getQuantity() + 1;
            currentBook.setQuantity(quantity);
            bookService.saveBook(currentBook);
        });
    }

    private void checkList(List<ReturnBorrowedRequest> requestList){
        if (requestList.isEmpty()){
            throw new BadRequestException(
                    resourceBundle.getString("service.return-book.empty-return-list"),
                    "service.return-book.empty-return-list");
        }
    }

    private List<BorrowedBookResponse> convertToBookResponse(List<Book> bookList){
        return bookList.stream()
                .map(book -> BorrowedBookResponse.builder()
                        .id(book.getId())
                        .picture(book.getPicture())
                        .title(book.getTitle())
                        .description(book.getDescription())
                        .author(book.getAuthor().getName())
                        .genre(book.getGenre().getName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<Book> toList(List<BorrowBookRequest> requestList){
        return requestList.stream()
                .map(book -> {
                    Book currentBook = bookService.findById(book.getId());
                    currentBook.setQuantity(currentBook.getQuantity() - 1);
                    bookService.saveBook(currentBook);
                    return currentBook;
                }).toList();
    }

    private void checkForActiveBorrowSession(){
        String email = Global.getCurrentLogin(resourceBundle).getUsername();
        boolean isActive = true;
        getReceiptByEmailAndActive(email, isActive).ifPresent((book) -> {
            throw new BadRequestException(
                    resourceBundle.getString("service.borrow-book.active-session"),
                    "service.borrow-book.active-session");
        });
    }

    private Optional<BorrowReceipt> getReceiptByEmailAndActive(String email, boolean isActive){
        return borrowReceiptRepository.getAllByAppUser_EmailAndActive(email, isActive);
    }

    private void isBookBorrowable(List<BorrowBookRequest> requestList){
        requestList
                .forEach(book -> {
                    Book currentBook = bookService.findById(book.getId());
                    if(currentBook.getQuantity() == 0){
                        throw new BadRequestException(
                                resourceBundle.getString("service.borrow-book.insufficient-inventory") + " " + currentBook.getTitle(),
                                "service.borrow-book.insufficient-inventory");
                    }

                    if(currentBook.getRemoved()){
                        throw new NotFoundException(
                                resourceBundle.getString("service.borrow-book.removed") + " " + currentBook.getTitle(),
                                "service.borrow-book.removed");
                    }
                });
    }

    private void checkBookPerGenre(List<BorrowBookRequest> requestList){
        Set<String> checkForUniqueGenre =  requestList.stream()
                .map(book -> bookService.findById(book.getId()).getGenre().getName())
                .collect(Collectors.toSet());

        if(checkForUniqueGenre.size() != requestList.size()){
            throw new BadRequestException(
                    resourceBundle.getString("service.borrow-book.not-unique-genre"),
                    "service.borrow-book.not-unique-genre");
        }
    }

    private void checkRequestList(List<BorrowBookRequest> requestList){
        if(requestList.isEmpty()){
            throw new BadRequestException(
                    resourceBundle.getString("service.borrow-book.empty-borrow-list"),
                    "service.borrow-book.empty-borrow-list");
        }
    }
}
