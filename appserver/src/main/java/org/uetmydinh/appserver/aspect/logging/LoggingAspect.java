package org.uetmydinh.appserver.aspect.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.stereotype.Repository *)"
            + "|| within(@org.springframework.stereotype.Service *)"
            + "|| within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }

    @Pointcut("within(org.uetmydinh.appserver..*)"
            + "|| within(org.uetmydinh.appserver.service..*)"
            + "|| within(org.uetmydinh.appserver.controller..*)")
    public void applicationPackagePointcut() {
    }

    @Pointcut("springBeanPointcut() && applicationPackagePointcut()")
    public void loggingPointcut() {
    }

    @AfterThrowing(pointcut = "loggingPointcut()", throwing = "e")
    public void logAfterThrowing(Exception e) {
        log.error("Exception in {}.{}() with cause = {}", e.getStackTrace()[0].getClassName(),
                e.getStackTrace()[0].getMethodName(), e.getCause() != null ? e.getCause() : "NULL");
    }


    @Around("loggingPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), joinPoint.getArgs());
        }

        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", joinPoint.getArgs(),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        } catch (Throwable e) {
            log.error("Exception in {}.{}()", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e);
            throw e;
        }
    }

}
