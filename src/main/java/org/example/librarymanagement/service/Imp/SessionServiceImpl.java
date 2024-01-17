package org.example.librarymanagement.service.Imp;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.librarymanagement.config.security.jwtConfig.JwtService;
import org.example.librarymanagement.entity.Session;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.repository.SessionRepository;
import org.example.librarymanagement.service.SessionService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ResourceBundle;

@AllArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final JwtService jwtService;
    private final ResourceBundle resourceBundle;

    @Override
    public void saveSession(String token){
        sessionRepository.save(new Session(
                jwtService.extractJti(token),
                jwtService.extractIssueAt(token),
                jwtService.extractExpiration(token)
        ));
    }

    @Override
    public Session getSessionWithJti(String jti){
        return sessionRepository.getSessionByJti(jti)
                .orElseThrow(() -> new NotFoundException("session.jti.jti-not-found",
                resourceBundle.getString("session.jti.jti-not-found")));
    }

    @Override
    public String getCurrentSessionJWT() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    @Override
    public void deactivateSession() {
        String jwtBearer = getCurrentSessionJWT();
        String jwt = jwtService.extractJwtToken(jwtBearer);
        sessionRepository.deactivateSession(jwtService.extractJti(jwt));
    }

}
