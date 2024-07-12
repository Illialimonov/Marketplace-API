package com.ilusha.marketplaceAPI.logging;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(LogExecution)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String parameters = (String) ((Stream) Arrays.stream(args).sequential())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        logger.info("Entering method: {}.{} with parameters: {}", className, methodName, parameters);

        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Exception in method: {}.{} with parameters: {}", className, methodName, parameters, throwable);
            throw throwable;
        }

        // After logic
        logger.info("Exiting method: {}.{}", className, methodName);

        return result;
    }
}