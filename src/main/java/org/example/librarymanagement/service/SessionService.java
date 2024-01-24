package org.example.librarymanagement.service;

import org.example.librarymanagement.entity.Session;
import org.example.librarymanagement.entity.base.Account;

public interface SessionService {
    void saveSession(String token, Account account);

    void updateSession(Session session);

    Session getSessionWithJti(String jti);

    String getCurrentSessionJWT();

    void deactivateSession();
}
