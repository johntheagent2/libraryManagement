package org.example.librarymanagement.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.librarymanagement.config.security.jwtConfig.JwtService;
import org.example.librarymanagement.service.SessionService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ResourceBundle;

@Aspect
@Component
@AllArgsConstructor
public class JtiCheckAspect {

    private final JwtService jwtService;
    private final SessionService sessionService;
    private final ResourceBundle resourceBundle;

    @Before("execution(* org.example.librarymanagement.controller.user..*(..)) " +
            "|| execution(* org.example.librarymanagement.controller.admin..*(..))")
    public void checkIfJtiIsValid(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwtTokenBearer = request.getHeader("Authorization");
        JwtService.checkJti(jwtTokenBearer, jwtService, sessionService, resourceBundle);
    }
}
