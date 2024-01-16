package org.example.librarymanagement.service.Imp;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.config.security.jwtConfig.JwtService;
import org.example.librarymanagement.entity.Session;
import org.example.librarymanagement.repository.SessionRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SessionServiceImpl {

    private final SessionRepository sessionRepository;
    private final JwtService jwtService;

    public void saveSession(String token){
        sessionRepository.save(new Session(
                jwtService.extractJti(token),
                jwtService.extractIssueAt(token),
                jwtService.extractExpiration(token)
        ));
    }

}
