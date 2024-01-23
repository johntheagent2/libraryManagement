package org.example.librarymanagement.common.aspect;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class Log4jAspect {

    @Before("execution(* org.example.librarymanagement.controller..*(..))")
    public void logBeforeControllerMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("Entering {}.{}", className, methodName);
    }

    @AfterReturning(pointcut = "execution(* org.example.librarymanagement.controller..*(..))", returning = "result")
    public void logAfterControllerMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("Exiting {}.{}. Returned: {}", className, methodName, result);
    }
}
