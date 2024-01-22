package org.example.librarymanagement.service;

import org.example.librarymanagement.entity.Session;

public interface SessionService {
    void saveSession(String token);

    void updateSession(Session session);

    Session getSessionWithJti(String jti);

    String getCurrentSessionJWT();

    void deactivateSession();
}
