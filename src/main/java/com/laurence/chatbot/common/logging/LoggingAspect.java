package com.laurence.chatbot.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Around("@annotation(com.laurence.chatbot.common.logging.LogExecutionTime)")
    public Object methodTimeLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        log.info("Execution time with class : {}, method : {}, and time : {} ms",
                proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                proceedingJoinPoint.getSignature().getName(),
                stopWatch.getTotalTimeMillis());

        return result;
    }
}
