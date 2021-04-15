package com.blackfat.debug.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-04-26 09:39
 * @since 1.0-SNAPSHOT
 */
@Aspect
@Component
@Order(10)
@Slf4j
public class TestAspectWithOrder20 {
    @Before("execution(* com.blackfat.debug.TestAopController.*(..))")
    public void before(JoinPoint joinPoint) throws Throwable {
        log.info("TestAspectWithOrder20 @Before");
    }

    @After("execution(* com.blackfat.debug.TestAopController.*(..))")
    public void after(JoinPoint joinPoint) throws Throwable {
        log.info("TestAspectWithOrder20 @After");
    }

    @Around("execution(* com.blackfat.debug.TestAopController.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("TestAspectWithOrder20 @Around before");
        Object o = pjp.proceed();
        log.info("TestAspectWithOrder20 @Around after");
        return o;
    }
}
