package org.example.librarymanagement.service;

public interface SmsSenderService {
    void sendSms(String receiver, String otp);
}
