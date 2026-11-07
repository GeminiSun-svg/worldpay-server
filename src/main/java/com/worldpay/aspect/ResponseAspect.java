package com.worldpay.aspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
public class ResponseAspect {

    @Autowired
    private MeterRegistry meterRegistry;

    @Around("execution(public * com.worldpay.service..*.*(..))")
    public Object unifyResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        // 定义一个Timer，用于记录方法执行时间
        Timer timer = Timer.builder("method.execution.time")
                .register(meterRegistry);

        // 记录方法开始时间
        long startTime = System.nanoTime();

        try {
            // 调用目标方法
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            // 处理异常
            throw throwable;
        } finally {
            // 计算方法执行时间并记录到Timer
            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;
            timer.record(Duration.ofNanos(executionTime));
            // 记录方法调用次数到Counter
            meterRegistry.counter("method.invocation.count").increment();
        }
    }
}
