package org.example.librarymanagement.service;

import org.example.librarymanagement.entity.Book;

import java.math.BigDecimal;
import java.util.List;

public interface EmailSenderService {
    void sendConfirmationMail(String token, String otp, String toEmail);

    void sendChangeRequest(String token, String toEmail);

    void sendResetRequest(String token, String toEmail);

    void changeResetSender(String toEmail, String tokenLink);

    void sendInvoiceEmail(String toEmail, List<Book> bookList, BigDecimal total);

    void sendMaintenanceMailToAllUser(List<String> toEmails);
}
