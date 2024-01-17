package org.example.librarymanagement.service.Imp;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.config.security.jwtConfig.JwtService;
import org.example.librarymanagement.entity.Session;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@AllArgsConstructor
@Service
public class SessionServiceImpl {

    private final SessionRepository sessionRepository;
    private final JwtService jwtService;
    private final ResourceBundle resourceBundle;

    public void saveSession(String token){
        sessionRepository.save(new Session(
                jwtService.extractJti(token),
                jwtService.extractIssueAt(token),
                jwtService.extractExpiration(token)
        ));
    }

    public Session getSessionWithJti(String jti){
        return sessionRepository.getSessionByJti(jti)
                .orElseThrow(() -> new NotFoundException("session.jti.jti-not-found",
                resourceBundle.getString("session.jti.jti-not-found")));
    }

}
