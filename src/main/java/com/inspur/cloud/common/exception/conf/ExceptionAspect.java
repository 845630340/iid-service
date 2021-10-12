package com.inspur.cloud.common.exception.conf;

import com.inspur.cloud.common.exception.handler.BaseExceptionHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * aspect for print exception stacktrace
 * @author mysterious guest
 */
@Aspect
@Component
public class ExceptionAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAspect.class);

    @Pointcut("execution(* com.inspur.cloud.fusionframework.common.exception.handler..*.*(..))")
    public void executePackage() {
    }

    @Before("executePackage()")
    public void beforeAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (Objects.nonNull(args) && args[0] instanceof Throwable) {
            LOGGER.error(BaseExceptionHandler.getStackTrace((Throwable) args[0]));
        }
    }

}
