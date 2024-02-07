package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.BorrowBookRequest;
import org.example.librarymanagement.dto.request.ReturnBorrowedRequest;
import org.example.librarymanagement.dto.response.BorrowedBookResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BorrowReceiptService {
    List<BorrowedBookResponse> getCurrentBorrowed();
    void borrow(List<BorrowBookRequest> requestList);
    void returnBorrowed(List<ReturnBorrowedRequest> requestList);
}
