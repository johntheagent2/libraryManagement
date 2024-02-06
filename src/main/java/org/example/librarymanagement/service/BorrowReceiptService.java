package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.BorrowBookRequest;
import org.example.librarymanagement.dto.response.BorrowedBookResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BorrowReceiptService {
    @Transactional(readOnly = true)
    List<BorrowedBookResponse> getCurrentBorrowed();

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    void borrow(List<BorrowBookRequest> requestList);
}
