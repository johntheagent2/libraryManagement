package org.example.librarymanagement.common.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Log4jAspect {

    private static final Logger logger = LogManager.getLogger("controller");

    @Before("execution(* org.example.librarymanagement.controller..*(..))")
    public void logBeforeControllerMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.info("Entering {}.{}", className, methodName);
    }

    @AfterReturning(pointcut = "execution(* org.example.librarymanagement.controller..*(..))", returning = "result")
    public void logAfterControllerMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.info("Exiting {}.{}. Returned: {}", className, methodName, result);
    }
}
